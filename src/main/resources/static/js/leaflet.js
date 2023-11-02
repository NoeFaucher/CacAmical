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

    if (!longitudeInput || !latitudeInput) {
        pointForm.style.display = "block";
        return;
    }

    // Récupérez les coordonnées du point cliqué
    var latitude = e.latlng.lat;
    var longitude = e.latlng.lng;

    // Pré-remplissez le formulaire avec les coordonnées
    latitudeInput.value = latitude;
    longitudeInput.value = longitude;

    // Affichez le formulaire
    pointForm.style.display = "block";
});


// Écoutez les frappes dans le champ d'adresse
document.getElementById('addressInput').addEventListener('input', function () {
    var address = this.value;

    // Appelez la fonction pour obtenir des suggestions d'adresse
    getAddressSuggestions(address);
});

// Fonction pour obtenir des suggestions d'adresse
function getAddressSuggestions(address) {

    // Utilisez l'API de géocodage en temps réel de Nominatim pour obtenir des suggestions d'adresse
    $.get(`https://nominatim.openstreetmap.org/search?format=json&q=${address}&limit=5`, function (data) {
        var suggestionsList = document.getElementById('suggestions');
        suggestionsList.innerHTML = '';

        if (data && data.length > 0) {
            data.forEach(function (result) {
                var suggestionItem = document.createElement('li');
                suggestionItem.textContent = result.display_name;
                suggestionItem.addEventListener('click', function () {
                    // Lorsque l'utilisateur clique sur une suggestion, géocodez l'adresse et pré-remplissez le formulaire
                    geocodeAndFillForm(result);
                    document.getElementById("addressInput").value = "";
                    document.getElementById("suggestions").innerHTML = "";
                });
                suggestionsList.appendChild(suggestionItem);
            });
        } else {
            suggestionsList.innerHTML = '<li>Aucune suggestion trouvée</li>';
        }
    });
}

// Fonction pour géocoder l'adresse et pré-remplir le formulaire
function geocodeAndFillForm(result) {
    var latitude = parseFloat(result.lat);
    var longitude = parseFloat(result.lon);

    // Pré-remplissez le formulaire avec les coordonnées
    document.getElementById("latitude").value = latitude;
    document.getElementById("longitude").value = longitude;

    // Affichez le formulaire
    document.getElementById("pointForm").style.display = "block";

    // Effacez les suggestions d'adresse
    document.getElementById('suggestions').innerHTML = '';
}

// Fonction pour effacer les champs du formulaire
function clearPointForm() {
    document.getElementById('latitude').value = '';
    document.getElementById('longitude').value = '';
    document.getElementById('description').value = '';
    document.getElementById('titre').value = '';
    // Réinitialisez le champ de note (s'il existe)
    var selectedRating = document.querySelector('input[name="stars"]:checked');
    if (selectedRating) {
        selectedRating.checked = false;
    }
}

// Fonction pour récupérer tous les points et les afficher sur la carte
function addPoint() {
    var latitude = document.getElementById('latitude').value;
    var longitude = document.getElementById('longitude').value;
    var description = document.getElementById('description').value;
    var titre = document.getElementById('titre').value;
    var selectedRating = document.querySelector('input[name="stars"]:checked');
    var note = selectedRating ? selectedRating.value : 0;
    var pointForm = document.getElementById("pointForm");

    pointForm.style.display = "";

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

            // Effacez les champs du formulaire après avoir ajouté le point
            clearPointForm();

            // Mettez à jour la carte avec le nouveau point
            var marker = L.marker([latitude, longitude], { icon: poopIcon }).addTo(map);
        },
        error: function (error) {
            console.error(error);
        }
    });

    return false;
}

// Fonction pour récupérer le nombre de likes d'un point
function getLikesCount(cacaId, callback) {
    $.get(`/getLikes/${cacaId}`, function (count) {
        callback(count);
    });
}

// Fonction pour récupérer les commentaires d'un point
function getComments(cacaId, callback) {
    $.get(`/getComments/${cacaId}`, function (comments) {
        callback(comments);
    });
}

// Fonction pour récupérer tous les points et les afficher sur la carte
function getAllPoints() {
    $.get("/getPoints", function (data) {
        $.get("/curUser", (userData) => {
            data.forEach(function (point) {
                var marker = L.marker([point.latitude, point.longitude], { icon: poopIcon }).addTo(map);

                // Créez un conteneur div pour le contenu de la popup
                var popupContainer = document.createElement('div');

                // Utilisez la fonction pour obtenir le nombre de likes
                getLikesCount(point.cacaId, function (count) {
                    // Créez un élément de texte pour afficher le nombre de likes
                    var likeCountText = document.createElement('span');
                    likeCountText.id = `like-count-${point.cacaId}`; // Identifiant unique
                    likeCountText.innerText = count;

                    // Utilisez la fonction pour obtenir les commentaires
                    getComments(point.cacaId, function (comments) {
                        // Créez un élément de texte pour afficher les commentaires
                        var commentsList = document.createElement('div');
                        commentsList.className = 'comments-list';

                        if (comments.length > 0) {
                            commentsList.innerHTML = "<p></p><p><strong>Commentaires:</strong><br></p>";
                            comments.forEach(function (comment) {
                                commentsList.innerHTML += `<em>${comment.user.nom} ${comment.user.prenom}:</em> ${comment.contenu}<br/>`;
                            });
                        }

                        // Créez un champ de texte pour le commentaire
                        var commentInput = document.createElement('input');
                        commentInput.id = 'comment-text'
                        commentInput.type = 'text';
                        commentInput.placeholder = 'Ajouter un commentaire...';
                        commentInput.style.display = 'none';

                        // Définissez un gestionnaire d'événements pour "keydown"
                        commentInput.addEventListener('keydown', function (event) {
                            if (event.key === 'Enter') {
                                // Si la touche "Entrée" est pressée, appelez la fonction addComment avec l'ID approprié
                                addComment(point.cacaId);
                            }
                        });

                        // Créez un contenu de popup avec les informations du point et un bouton de suppression
                        var popupContent = `
                            <h3>${point.titre}</h3>
                            <p><strong>De:</strong> ${point.user.nom} ${point.user.prenom}</p>
                            <p><strong>Description:</strong> ${point.description}</p>
                            <p><strong>Latitude:</strong> ${point.latitude}</p>
                            <p><strong>Longitude:</strong> ${point.longitude}</p>
                            <p><strong>Note:</strong> ${point.note}</p>
                        `;

                        if (userData.hasOwnProperty("userId") && point.user.userId == userData.userId) {
                            popupContent += `
                            <button onclick="editPoint(${point.cacaId})">Modifier</button>
                            <button onclick="deletePoint(${point.cacaId})">Supprimer</button>
                            <button onclick="addComment(${point.cacaId})">Commenter</button>
                            `
                            commentInput.style.display = 'block';

                        }
                        popupContent += `
                            <button onclick="likePoint(${point.cacaId})"> ❤️ </button>   
                            `;

                        // Ajoutez le contenu de la popup
                        popupContainer.innerHTML += popupContent;
                        popupContainer.appendChild(likeCountText);
                        popupContainer.appendChild(commentInput);
                        popupContainer.appendChild(commentsList);

                        // Créez la popup avec le contenu
                        marker.bindPopup(popupContainer);

                        // Définissez un gestionnaire d'événements pour ouvrir la popup au clic
                        marker.on('click', function () {
                            marker.openPopup();
                        });
                    });
                });
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

            // Met à jour le nombre de likes en appelant la fonction getLikesCount
            getLikesCount(cacaId, function (count) {
                // Sélectionnez l'élément contenant le nombre de likes et mettez à jour son contenu
                var likeCountElement = document.querySelector(`#like-count-${cacaId}`);
                if (likeCountElement) {
                    likeCountElement.innerText = count;
                }
            });
        },
        error: function (error) {
            alert("Vous avez deja liker le points.");
            console.error(error);
        }
    });
}

// Fonction pour ajouter un commentaire
function addComment(cacaId) {
    var commentContent = document.getElementById('comment-text').value;

    // Vérifiez si le commentaire n'est pas vide
    if (commentContent.trim() === '') {
        alert("Veuillez entrer un commentaire.");
        return;
    }

    var data = {
        contenu: commentContent
    };

    $.ajax({
        type: "POST",
        url: `/addComment/${cacaId}`,
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
            // Réaffichez tous les points après l'ajout du commentaire
            getAllPoints();
        },
        error: function (error) {
            alert("Erreur lors de l'ajout du commentaire.");
            console.error(error);
        }
    });
}

// Appelez la fonction pour récupérer et afficher les points lorsque la page est chargée
$(document).ready(function () {
    getAllPoints();
});

document.getElementById('hideButton').addEventListener('click', function() {
    var pointForm = document.getElementById('pointForm');
    pointForm.style.display = 'none';
});