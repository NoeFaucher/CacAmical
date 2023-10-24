const urlParams = new URLSearchParams(window.location.search);
const isError = urlParams.get("error");
const isLogout = urlParams.get("logout");


// Check if the "error" parameter exists in the URL
if (isError != null) {
  // Show the error message
  const errorMessage = document.getElementById("error-message");
  errorMessage.classList.remove("hidden");
}

if (isLogout != null) {
  const logoutMessage = document.getElementById("logout-message");
  logoutMessage.classList.remove("hidden");
}