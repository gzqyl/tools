<?php
	
	class RSA_PHP{

		//could only encode len(private_rsa.key)/8 bytes char

		/**installed openssl

		* openssl genrsa -out rsa_private_key.pem 1024[长度]
		* openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem

		*/

		public function __construct(){

			$private_key = file_get_contents("rsa_private_key.pem");
			$public_key = file_get_contents("rsa_public_key.pem");

			$this->private_key = openssl_pkey_get_private($private_key);

			$this->public_key = openssl_pkey_get_public($public_key);

		}

		//公钥加密，私钥解密

		public function encrypt($data){

			$data = base64_encode($data); //1024/8

			openssl_public_encrypt($data,$encrypted,$this->public_key);

			return $encrypted;

			//encrypted data will be in binary format...

		}

		public function decrypt($data){

			openssl_private_decrypt($data,$decrypted,$this->private_key);

			return base64_decode($decrypted);

		}


	}
	
	

	
