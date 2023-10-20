var map = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

var poopIcon = L.icon({
    iconUrl: '/resources/static/img/poop.png',
    iconSize: [45, 45],
});

// Ajoutez un gestionnaire d'événements "click" sur la carte
map.on('click', function (e) {
    var marker = L.marker(e.latlng, { icon: poopIcon }).addTo(map);
    marker.bindPopup("<b>Hello world!</b><br>I am a poop.").openPopup();
});
