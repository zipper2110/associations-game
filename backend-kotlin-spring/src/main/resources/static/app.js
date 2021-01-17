var stompClient = null;

let userToken = null

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/game');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        $(".websocket-status").text("подключён")
        stompClient.subscribe('/topic/*', function (greeting) {
            console.log(greeting.body);
            showGreeting(greeting.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
    $(".websocket-status").text("отключён")
}

function sendCommand() {
    let message = JSON.parse($("#content").val())
    message.token = userToken
    stompClient.send("/game/" + $("#path").val(), {}, JSON.stringify(message));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function logIn() {
    let username = $("#username").val();
    fetch("/auth", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username })
    }).then(response => {
        response.json().then(json => {
            let status = "успешно вошли"
            if (response.status !== 200) {
                status = "не удалось войти"
            } else {
                setToken(json.token)
            }
            $("#auth-status").text(status)
            log("авторизация", status)
        })
    })
}

function logOut() {
    fetch("/logout", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token: userToken })
    }).then(response => {
        let status
        if (response.status === 200) {
            status = "вышли"
            setToken("")
        } else {
            status = "ошибка при выходе"
        }
        $("#auth-status").text(status)
        log("авторизация", status)
    })
}

function getUser() {
    fetch("/me?token=" + userToken).then(response => response.json().then( json => {
        let user
        if (response.status !== 200) {
            user = "ошибка"
        } else {
            user = JSON.stringify(json, null, 2)
        }
        $("#user-info").text(user)
    }))
}

function log(source, message) {
    $(".websocket-log").add(`<p>${source + ": " + message}</p>`)
}

function setToken(token) {
    userToken = token
    $("#token").val(userToken)
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send-command" ).click(function() { sendCommand(); });
    $( "#login" ).click(function() { logIn(); });
    $( "#logout" ).click(function() { logOut(); });
    $( "#get-user" ).click(function() { getUser(); });
});