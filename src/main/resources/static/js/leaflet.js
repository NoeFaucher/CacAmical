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
                <button onclick="deletePoint(${point.cacaId})">Supprimer</button>
            `;

            // Ajoutez le popup au marqueur
            marker.bindPopup(popupContent);

            // Définissez un gestionnaire d'événements pour ouvrir le popup au clic
            marker.on('click', function () {
                marker.openPopup();
            });
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
            console.error(error);
        }
    });
}


// Appelez la fonction pour récupérer et afficher les points lorsque la page est chargée
$(document).ready(function () {
    getAllPoints();
});
