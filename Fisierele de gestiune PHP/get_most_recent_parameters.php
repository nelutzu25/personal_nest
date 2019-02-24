<?php 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'include/db_connect.php';
 
// connecting to db 
$db = new DB_CONNECT();
 
// check for post data
if (isset($_POST['device_id']) && $_POST['device_id'] !='') {
 
    $device_id = $_POST['device_id']; 
    // get a temperature from parameters table
    $result = mysql_query("SELECT * FROM parameters WHERE device_id = $device_id ORDER BY id DESC LIMIT 1");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $parameter = array();
			$parameter["id"] = $result["id"]; 
            $parameter["temperature"] = $result["temperature"];
            $parameter["humidity"] = $result["humidity"]; 
            $parameter["date"] = $result["date"];
            // success
            $response["success"] = 1;
 
			$resultd = mysql_query("SELECT * FROM desired_temperatures WHERE device_id = $device_id");
			$resultd = mysql_fetch_array($resultd);
			$parameter["desired_temperature"] = $resultd["desired_temperature"];
 
            // user node
            $response["parameter"] = array();
 
            array_push($response["parameter"], $parameter);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no temperature found 
            $response["success"] = 0;
            $response["message"] = "No parameters found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no temperature found
        $response["success"] = 0;
        $response["message"] = "No parameters found";
 
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