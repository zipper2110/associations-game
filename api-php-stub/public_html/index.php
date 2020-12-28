<?php

# Secret credentials
$user = [
    'id' => 1,
    'username' => 'test',
    'password' => 'test',
];
$userWthoutPassword = $user;
unset($userWthoutPassword['password']);

$game = [
    'title' => 'First game',
    'users_number' => 1,
    'decks_number' => 3,
    'leader_id' => 1,
    'users' => [
        $userWthoutPassword
    ],
];

$jwt_token = [
    'access_token' => 'secret',
    'access_type' => 'bearer',
    'expires_in' => 3600,
];


# Import and init API
require_once('Api.class.php');
$api = new Api();
$api->secret_token = $jwt_token['access_token'];


# Main Page
$api->get('/', function() use ($api) {
    $api->respond(200, [
        'status' => 'ok',
        'message' => 'AG API v0.0.1',
    ]);
});

######################  PLAYER  ######################

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
$api->post('/user/logout', function() use ($api) {
    $api->checkAccess();
    $api->respond(200);
});

# Refresh JWT token
$api->get('/user/refresh-token', function() use ($api, $jwt_token) {
    $api->checkAccess();
    $api->respond(200, $jwt_token);
});

# Get User
$api->get('|^/user/(\d+)$|', function($params) use ($api, $userWthoutPassword) {
    $api->checkAccess();
    if ((int) $params[1] !== 1) {
        $api->respond(404);
    }
    $api->respond(200, $userWthoutPassword);

}, Api::TYPE_REGEXP);


######################  GAME  ######################

# Create a new game
$api->post('/game/create', function() use ($api, $game) {
    $api->checkAccess();
    $api->respond(200, $game);
});

# Add a player to a game
$api->post('|^/game/(\d+)/player$|', function($params) use ($api) {
    $api->checkAccess();
    if ((int) $params[1] !== 1) {
        $api->respond(404);
    }
    $api->respond(200);

}, Api::TYPE_REGEXP);

# Remove a player from a game
$api->delete('|^/game/(\d+)/player$|', function($params) use ($api) {
    $api->checkAccess();
    if ((int) $params[1] !== 1) {
        $api->respond(404);
    }
    $api->respond(200);

}, Api::TYPE_REGEXP);

# Get the game state
$api->get('|^/game/(\d+)$|', function($params) use ($api, $game) {
    $api->checkAccess();
    if ((int) $params[1] !== 1) {
        $api->respond(404);
    }
    $api->respond(200, $game);

}, Api::TYPE_REGEXP);



# Run the API
$api->run();
