<?php

	session_start();

	header("Content-type:image/png");

	$im = imagecreatetruecolor(100,30);

	$bgcolor = imagecolorallocate($im,255,mt_rand(100,255),mt_rand(100,255));

	$textcolor = imagecolorallocate($im,0,mt_rand(0,100),mt_rand(0,100));

	imagefill($im,0,0,$bgcolor);

	$code = strval(mt_rand(1111,9999));

	$_SESSION['code'] = $code;

	//分布验证码

	$code_x = 15;

	for($i=0;$i<4;$i++){

		imagestring($im,5,$code_x,10,$code[$i],$textcolor);

		$code_x += 20;

	}

	

	for($i=0;$i< 10;$i++){

		//设定干扰线起始点

		$start_x = mt_rand(10,60);

		$start_y = mt_rand(0,30);

		//设定干扰线结束点

		$end_x = mt_rand($start_x + 10,90);

		$end_y = mt_rand($start_y >= 15 ? 0 : 20,$start_y >= 15 ? 10 : 30);

		$linecolor = imagecolorallocate($im,mt_rand(0,255),mt_rand(0,255),mt_rand(0,255));

		imageline($im,$start_x,$start_y,$end_x,$end_y,$linecolor);

	}
	

	imagepng($im);