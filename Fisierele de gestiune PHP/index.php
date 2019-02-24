<?php
 /**
 * Each request will be identified by TAG
 * Response will be JSON data
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'include/db_functions.php';
    $db = new DB_FUNCTIONS();
 
    // response Array
    $response = array("tag" => $tag, "error" => FALSE);
 
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) { 
            // user found
            $response["error"] = FALSE;
            $response["uid"] = $user["uid"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
			$response["user"]["device_id"] = $user["device_id"];
			$response["user"]["comfort_index"] = $user["comfort_index"];
			$response["user"]["notification"] = $user["notification"];
			$response["user"]["notify_min_temp"] = $user["notify_min_temp"];
			$response["user"]["notify_max_temp"] = $user["notify_max_temp"];
			$response["user"]["notify_min_humidity"] = $user["notify_min_humidity"];
			$response["user"]["notify_max_humidity"] = $user["notify_max_humidity"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = TRUE;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
		$device_id = $_POST['device_id'];
 
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = TRUE;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else { 
			 if (!$db->isDeviceIdValid($device_id)) {
				// user is already existed - error response
				$response["error"] = TRUE;
				$response["error_msg"] = "Device id is invalid"; 
				echo json_encode($response);
			}else{
				// store user
				$user = $db->storeUser($name, $email, $password, $device_id);
				if ($user) {
					// user stored successfully
					$response["error"] = FALSE;
					$response["uid"] = $user["uid"];
					$response["user"]["name"] = $user["name"];
					$response["user"]["email"] = $user["email"];
					$response["user"]["created_at"] = $user["created_at"];
					$response["user"]["updated_at"] = $user["updated_at"];
					$response["user"]["device_id"] = $user["device_id"];
					echo json_encode($response);
				} else {
					// user failed to store
					$response["error"] = TRUE;
					$response["error_msg"] = "Error occurred in Registration";
					echo json_encode($response);
				}
			}
        }
    } else {
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown 'tag' value. It should be either 'login' or 'register'";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>