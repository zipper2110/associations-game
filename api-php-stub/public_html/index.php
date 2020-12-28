<?php

# Import and init API
require_once('Api.class.php');
$api = new Api();


# Secret credentials
$user = [
    'id' => 1,
    'username' => 'test',
    'password' => 'test',
];

$jwt_token = [
    'access_token' => 'secret',
    'access_type' => 'bearer',
    'expires_in' => 3600,
];


# Main Page
$api->get('/', function() use ($api) {
    $api->respond(200, [
        'status' => 'ok',
        'message' => 'AG API v0.0.1',
    ]);
});

# Register a New User
$api->post('/user/register', function() use ($api, $jwt_token) {
    if ($api->data['username'] && $api->data['password']) {
        $api->respond(200, $jwt_token);
    }
    $api->respond(400);
});

# Login
$api->post('/user/login', function() use ($api, $user, $jwt_token) {
    if ($api->data['username'] === $user['username'] && $api->data['password'] === $user['password']) {
        $api->respond(200, $jwt_token);
    }
    $api->respond(401);
});

# Logout
$api->post('/user/logout', function() use ($api, $jwt_token) {
    if ($api->token !== $jwt_token['access_token']) {
        $api->respond(401);
    }
    $api->respond(200);
});

# Refresh JWT token
$api->get('/user/refresh-token', function() use ($api, $jwt_token) {
    if ($api->token !== $jwt_token['access_token']) {
        $api->respond(401);
    }
    $api->respond(200, $jwt_token);
});

# Get User
$api->get('|^/user/(\d+)$|', function($params) use ($api, $user, $jwt_token) {
    if ($api->token !== $jwt_token['access_token']) {
        $api->respond(401);
    }
    if ((int) $params[1] !== 1) {
        $api->respond(404);
    }
    unset($user['password']);
    $api->respond(200, $user);

}, Api::TYPE_REGEXP);


# Run the API
$api->run();
