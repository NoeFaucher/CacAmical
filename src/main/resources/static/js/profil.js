

// variable countaining all the users 
const fuse = new Fuse([],{
    keys: [
        'nom',
        'prenom'
    ]
});
var filteredUsers = [];



// function to set the users variable
function setUsers() {
    $.get("/getUsers", (fetchedUsers) => {
        console.log(fetchedUsers);
        fuse.setCollection(fetchedUsers);
        console.log(fetchedUsers);
    });
}

// function called when key is pressed in the search friend bar
function lookForFriend(value) {
    filteredUsers = fuse.search(value)
    
    // display the list
    displaySearchUsers();
}
 
// function that display the list of user corresponding to the search
function displaySearchUsers() {
    // get the div element
    let div = document.getElementById("list-display")

    // remove the current content
    div.innerHTML = "";

    // display in the div with the 5 first users
    filteredUsers.slice(0, 5).forEach(item => {
        let user = item.item;
        let userDiv = document.createElement('div');
        userDiv.className = "bg-gray-100 p-2 rounded-md cursor-pointer transition duration-300 ease-in-out hover:bg-gray-200 flex justify-between items-center"
        userDiv.innerHTML = `<div><span class="text-blue-500">${user.nom}</span> <span class="text-gray-600">${user.prenom}</span><div> <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded-lg">Ajouter</button>`;

        userDiv.onclick = () => {
            addFriend(user.userId,userDiv)
        }

        div.appendChild(userDiv);
    });

}


// function to add a friend
function addFriend(userId,ele) {
    $.ajax({
        type: "POST",
        url: "/addFriend",
        data: JSON.stringify({"friendId" : userId}),
        contentType: "application/json",
        success: function (response) {
            alert(response)
            console.log(ele);

            let nom = ele.children[0].children[0].innerHTML;
            let prenom = ele.children[0].children[1].innerHTML;

            let list = document.getElementById("friend-list");

            noFriendItem = document.getElementById("no-friend");
            if(noFriendItem){
                list.removeChild(noFriendItem);
            }

            
            let friendDiv = document.createElement('li');
            friendDiv.className = "mb-2 text-grey-600 cursor-pointer"
            friendDiv.innerHTML = `<span>${nom}</span> <span class="ml-2">${prenom}</span>`;
            friendDiv.onclick = () => deleteFriend(userId,friendDiv);

            list.appendChild(friendDiv);
        }
    });
}

// function to remove a friend
function deleteFriend(userId,ele) {
    $.ajax({
        type: "DELETE",
        url: "/removeFriend",
        data: JSON.stringify({"friendId" : userId}),
        contentType: "application/json",
        success: function (response) {
            alert(response);
            ele.parentElement.removeChild(ele)
            
        }
    });
}



// set the event listener that trigger search friend function
document.getElementById("find-friend-field").addEventListener("keyup",(e) => {
    lookForFriend(e.target.value)
})

// set the users when the page is loaded
setUsers();