<?php

class Api
{
    public $headers;
    public $optionsHeaders;
    public $apiPrefix;
    public $token;
    public $data;

    private $method;
    private $route;
    private $routes;

    /**
     * Api constructor.
     */
    public function __construct()
    {
        # Set default options
        $this->headers = [
            'Access-Control-Allow-Origin' => '*',
            'Access-Control-Allow-Headers' => 'Origin, X-Requested-With, Content-Type, Accept, api_key, Authorization',
        ];
        $this->optionsHeaders = [
            'Access-Control-Allow-Methods' => 'GET, POST, DELETE, PUT, PATCH, OPTIONS',
        ];
        $this->apiPrefix = '/v1/';
        $this->method = $_SERVER['REQUEST_METHOD'];
        $this->route = $_SERVER['REQUEST_URI'];
        $this->token = $this->getBearerToken();
        $this->data = $this->getData();
        $this->routes = [];
    }

    /**
     * Run the API
     */
    public function run()
    {
        $this->setHeaders();
        $this->processRoutes();
        $this->respond(404);
    }

    /**
     * Add a GET route
     *
     * @param string $route
     * @param $callback
     * @param bool $regexp
     */
    public function get($route, $callback, $regexp = false)
    {
        $this->routes[] = [
            'method' => 'GET',
            'route' => $route,
            'callback' => $callback,
            'regexp' => $regexp,
        ];
    }

    /**
     * Add a POST route
     *
     * @param string $route
     * @param $callback
     * @param bool $regexp
     */
    public function post($route, $callback, $regexp = false)
    {
        $this->routes[] = [
            'method' => 'POST',
            'route' => $route,
            'callback' => $callback,
            'regexp' => $regexp,
        ];
    }

    /**
     * Basic responses
     *
     * @param int $code
     * @param array $data
     * @param string $message
     */
    public function respond($code = 200, $data = [], $message = '')
    {
        if ($code === 200) {
            header("HTTP/1.1 200 OK");
            if (self::ca($data)) {
                header("Content-Type: application/json");
                echo json_encode($data);
            }
            die;
        }
        if ($code === 400) {
            header("HTTP/1.1 400 Invalid request");
            die;
        }
        if ($code === 401) {
            header("HTTP/1.1 401 Unauthorised access");
            die;
        }
        if ($code === 404) {
            header("HTTP/1.1 404 Page not found");
            die;
        }
        header("HTTP/1.1 {$code} {$message}");
        die;
    }

    /**
     * Set the provided headers
     */
    private function setHeaders()
    {
        foreach ($this->headers as $k => $v) {
            header("{$k}: {$v}");
        }
        if ($this->method === 'OPTIONS') {
            foreach ($this->optionsHeaders as $k => $v) {
                header("{$k}: {$v}");
            }
            header("HTTP/1.1 200 OK");
            die;
        }
    }

    /**
     * Process routes
     */
    private function processRoutes()
    {
        # remove API prefix from the route
        $this->route = str_replace($this->apiPrefix, '/', $this->route);

        # check routes one by one
        foreach ($this->routes as $route)
        {
            if ($this->method === $route['method'])
            {
                # Proecss simple routes
                if (!$route['regexp'] && $this->route === $route['route']) {
                    $route['callback']();
                }

                # Proecss regexp routes
                if ($route['regexp'] && preg_match($route['route'], $this->route, $matches)) {
                    $route['callback']( $matches );
                }
            }
        }
    }

    /**
     * Set JSON data from the request body
     */
    private function getData()
    {
        return json_decode(file_get_contents('php://input'), true);
    }
    
    /** 
     * Get header Authorization
     */
    private function getAuthorizationHeader()
    {
        $headers = null;
        if (isset($_SERVER['Authorization'])) {
            $headers = trim($_SERVER["Authorization"]);
        }
        elseif (isset($_SERVER['HTTP_AUTHORIZATION'])) {
            $headers = trim($_SERVER["HTTP_AUTHORIZATION"]);
        }
        elseif (function_exists('apache_request_headers')) {
            $requestHeaders = apache_request_headers();
            $requestHeaders = array_combine(
                array_map('ucwords', array_keys($requestHeaders)), array_values($requestHeaders)
            );
            if (isset($requestHeaders['Authorization'])) {
                $headers = trim($requestHeaders['Authorization']);
            }
        }
        return $headers;
    }
    
    /**
     * Get access token from header
     */
    private function getBearerToken()
    {
        $headers = $this->getAuthorizationHeader();
        if (!empty($headers) && preg_match('/Bearer\s(\S+)/', $headers, $matches)) {
            return $matches[1];
        }
        return null;
    }

    /**
     * Check if this is a not empty array
     *
     * @param $array
     * @return bool
     */
    private static function ca($array) {
        return is_array($array) && count($array) > 0;
    }
}
