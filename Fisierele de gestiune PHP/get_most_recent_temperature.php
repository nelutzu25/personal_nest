<?php
 
/*
 * Following code will get single temperature details
 * A temperature is identified by temperature id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'include/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_POST['device_id'])) {
 
    $device_id = $_POST['device_id']; 
    // get a temperature from temperatures table
    $result = mysql_query("SELECT * FROM temperatures WHERE device_id = $device_id ORDER BY id DESC LIMIT 1");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $temperature = array();
			$temperature["id"] = $result["id"]; 
            $temperature["temperature"] = $result["temperature"];
            $temperature["date"] = $result["date"];
            // success
            $response["success"] = 1;
 
			$resultd = mysql_query("SELECT * FROM desired_temperatures WHERE device_id = $device_id");
			$resultd = mysql_fetch_array($resultd);
			$temperature["desired_temperature"] = $resultd["desired_temperature"];
 
            // user node
            $response["temperature"] = array();
 
            array_push($response["temperature"], $temperature);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no temperature found 
            $response["success"] = 0;
            $response["message"] = "No temperature found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no temperature found
        $response["success"] = 0;
        $response["message"] = "No temperature found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>