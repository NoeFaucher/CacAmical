var map = L.map('map').setView([43.33, -0.36], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

var poopIcon = L.icon({
    iconUrl: '/resources/static/img/poop.png',
    iconSize: [45, 45],
});

// Ajoutez un gestionnaire d'événements "click" sur la carte
map.on('click', function (e) {
    var pointForm = document.getElementById("pointForm");
    var latitudeInput = document.getElementById("latitude");
    var longitudeInput = document.getElementById("longitude");


    // Récupérez les coordonnées du point cliqué
    var latitude = e.latlng.lat;
    var longitude = e.latlng.lng;

    // Pré-remplissez le formulaire avec les coordonnées
    latitudeInput.value = latitude;
    longitudeInput.value = longitude;

    // Affichez le formulaire
    pointForm.style.display = "block";
});

// Fonction pour récupérer tous les points et les afficher sur la carte
function addPoint() {
    var latitude = document.getElementById('latitude').value;
    var longitude = document.getElementById('longitude').value;
    var description = document.getElementById('description').value;
    var titre = document.getElementById('titre').value;
    var selectedRating = document.querySelector('input[name="stars"]:checked');
    var note = selectedRating ? selectedRating.value : 0;
    console.log(note);

    var data = {
        longitude: longitude,
        latitude: latitude,
        description: description,
        titre: titre,
        note: note
    };

    // Utilisez AJAX pour envoyer les données au serveur
    $.ajax({
        type: "POST",
        url: "/addPoint",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            // La réponse du serveur peut contenir un message ou des données supplémentaires, en fonction de votre implémentation côté serveur.
            console.log(response);

            // Mettez à jour la carte avec le nouveau point
            var marker = L.marker([latitude, longitude], { icon: poopIcon }).addTo(map);
        },
        error: function (error) {
            console.error(error);
        }
    });
    return false;
}

// Fonction pour récupérer tous les points et les afficher sur la carte
function getAllPoints() {
    $.get("/getPoints", function (data) {
        data.forEach(function (point) {
            var marker = L.marker([point.latitude, point.longitude], { icon: poopIcon }).addTo(map);

            // Créez un contenu de popup avec les informations du point et un bouton de suppression
            var popupContent = `
                <h3>${point.titre}</h3>
                <p><strong>Description:</strong> ${point.description}</p>
                <p><strong>Latitude:</strong> ${point.latitude}</p>
                <p><strong>Longitude:</strong> ${point.longitude}</p>
                <p><strong>Note:</strong> ${point.note}</p>
                <button onclick="editPoint(${point.cacaId})">Modifier</button>
                <button onclick="deletePoint(${point.cacaId})">Supprimer</button>
                <button class="like-button" onclick="likePoint(${point.cacaId})" data-caca-id="${point.cacaId}">
                        <span class="like-count"></span> ❤️ Like            `;

            // Ajoutez le popup au marqueur
            marker.bindPopup(popupContent);

            // Définissez un gestionnaire d'événements pour ouvrir le popup au clic
            marker.on('click', function () {
                marker.openPopup();
            });
            updateLikeCount(point.cacaId);

        });
    });
}

// Fonction pour supprimer un point
function deletePoint(cacaId) {
    $.ajax({
        type: "DELETE",
        url: `/deletePoint/${cacaId}`,
        success: function (response) {
            console.log(response);
            // Rafraîchissez la carte pour supprimer le marqueur
            map.eachLayer(function (layer) {
                if (layer instanceof L.Marker) {
                    map.removeLayer(layer);
                }
            });
            // Réaffichez tous les points après la suppression
            getAllPoints();
        },
        error: function (error) {
            alert("Vous n'êtes pas autorisé à supprimer ce point ou veuillez vous connecter pour supprimer !");
            console.error(error);
        }
    });
}

// Fonction pour éditer un point
function editPoint(cacaId) {
    var newDescription = prompt("Entrez la nouvelle description :");
    if (newDescription) {
        var data = {
            description: newDescription
        };

        $.ajax({
            type: "PUT",
            url: `/editPoint/${cacaId}`,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                console.log(response);
                // Rafraîchissez la carte pour refléter les modifications
                map.eachLayer(function (layer) {
                    if (layer instanceof L.Marker) {
                        map.removeLayer(layer);
                    }
                });
                // Réaffichez tous les points après la modification
                getAllPoints();
            },
            error: function (error) {
                alert("Vous n'êtes pas autorisé à modifier ce point ou veuillez vous connecter pour modifier !");
                console.error(error);
            }
        });
    }
}

// Fonction pour gérer le "like" d'un point
function likePoint(cacaId) {
    $.ajax({
        type: "POST",
        url: `/addLike/${cacaId}`,
        success: function (response) {
            console.log(response);
            // Mettez à jour l'interface utilisateur pour refléter que l'utilisateur a "aimé" le point (par exemple, en changeant la couleur du bouton)
        },
        error: function (error) {
            alert("Vous devez être connecté pour aimer un point.");
            console.error(error);
        }
    });
}

// Fonction pour mettre à jour le nombre de likes à côté du bouton "Like"
function updateLikeCount(cacaId) {
    $.get(`/getLikes/${cacaId}`, function (likeCount) {
        // Recherchez le bouton "Like" dans la popup du point
        console.log("Like js Récupérés" + likeCount);
        var likeButton = document.querySelector(`[data-caca-id="${cacaId}"] .like-button .like-count`);

        if (likeButton) {
            // Affichez le nombre de likes à côté du bouton
            likeButton.innerHTML = likeCount;
        }
    });
}


// Appelez la fonction pour récupérer et afficher les points lorsque la page est chargée
$(document).ready(function () {
    getAllPoints();
});
