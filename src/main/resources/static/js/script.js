function showSignUp(e) {
    document.querySelectorAll(".sign-in-container").forEach(x => x.style.display = "none")
    document.querySelectorAll(".sign-up-container").forEach(x => x.style.display = "block")
    document.querySelectorAll(".sign-in-overlay").forEach(x => x.style.display = "none")
    document.querySelectorAll(".sign-up-overlay").forEach(x => x.style.display = "flex")
    e.preventDefault();
}

function showSignIn(e) {
    document.querySelectorAll(".sign-in-container").forEach(x => x.style.display = "block")
    document.querySelectorAll(".sign-up-container").forEach(x => x.style.display = "none")
    document.querySelectorAll(".sign-in-overlay").forEach(x => x.style.display = "flex")
    document.querySelectorAll(".sign-up-overlay").forEach(x => x.style.display = "none")
    e.preventDefault();
}

function saveFormData(formId, e) {
    const form = document.getElementById(formId);
    const inputs = document.querySelectorAll(`#${formId} input`);
    const formData = {};
    inputs.forEach(input => {
        formData[input.name] = input.value;
    });

    const xhr = new XMLHttpRequest();
    xhr.open("POST", form.action, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.onload = function() {
      if (xhr.responseText == "") {
        alert("Failed...");
      } else {
        alert("Success!");
      }
    };

    xhr.onerror = function() {
      alert("Failed...");
    };
    xhr.send(JSON.stringify(formData));
    e.preventDefault();
}

var xhr = new XMLHttpRequest();
xhr.open("GET", "/api/user", true);
xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
xhr.onload = function() {
  if (xhr.responseText == "") {
    // TODO: handle the case when user isn't logged in
    console.log("Not signed in.");
  } else {
    // TODO: handle the user
    var user = JSON.parse(xhr.responseText);
    console.log("Username: " + user.name);
    console.log("Email: " + user.email);
    console.log("Password: " + user.password);
  }
};

xhr.onerror = function() {
  alert(`Error while fetching user occurred`);
};

xhr.send();
