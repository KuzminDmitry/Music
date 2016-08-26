//Controller Part
app.controller("SecurityController", function($scope, $http, $rootScope, $cookies) {
	
	$rootScope.logged = false;	
	
	$scope.loginForm = {
        username : "",
        password : ""
    };
	
	checkLogin();

	
	  /* Private Methods */
    function checkLogin() {
		if($cookies.get("authdata")!=null){
			$rootScope.logged = true;
		}else{	
			$rootScope.logged = false;	
		}
    }

    $scope.login = function() {
		if($scope.validateLoginForm()){
			$cookies.put("cookieEnableTest", "cookieEnableTest");     
			$http({
				method : "POST",
				url : 'rest/security/login',
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8',
					'username' : $scope.loginForm.username,
					'password' : $scope.loginForm.password
				}
			}).then(function successCallback(response) {
				$cookies.put("authdata", response.data.jwt);     
				$rootScope.logged = true;   			
				$scope.loginFormValidation.accessDenied = false;
				$cookies.remove("cookieEnableTest", "cookieEnableTest"); 
			}, function errorCallback(response) {
				$scope.loginFormValidation.accessDenied = true;
				if(response.status == 400){
					$scope.loginFormValidation.accessDeniedMessage = "Cookies are not enabled!";					
				}
				if(response.status == 401){
					$scope.loginFormValidation.accessDeniedMessage = "Wrong username or password!";					
				}
				if(response.status == 500){
                    $scope.loginFormValidation.accessDeniedMessage = "Server cannot respond!";
                }
				console.log(response.statusText);
			});
		}
    }
	$scope.logout = function() {
        if($cookies.get("authdata")!=null){
            $http({
                method : "POST",
                url : 'rest/security/logout',
                headers : {
                    'Content-Type' : 'application/json; charset=UTF-8',
					'jwt' : $cookies.get("authdata")
                }
            }).then(function successCallback(response) {
                $cookies.remove("authdata");
                $rootScope.logged = false;
            }, function errorCallback(response) {
                console.log(response.statusText);
            });
        }
	}
	
	$scope.loginFormValidation = {
        usernameError : false,		
		passwordError : false,
		accessDenied : false,	
		usernameErrorMessage: "",
		passwordErrorMessage : "",
		accessDeniedMessage : ""
    };
	
	$scope.validateLoginForm = function(){
		$scope.loginFormValidation.accessDeniedMessage = "";
		var regexp = /[а-яё]/i;
		 if(regexp.test($scope.loginForm.username) || regexp.test($scope.loginForm.password)) {
		   $scope.loginFormValidation.accessDenied = true;
		   $scope.loginFormValidation.accessDeniedMessage = "Don't use cyrillic symbols!";	
		   return false;
		}
		var result = true;
		//Username
		if($scope.loginForm.username == null || $scope.loginForm.username.length == 0){
			$scope.loginFormValidation.usernameError = true;
			$scope.loginFormValidation.usernameErrorMessage = "Username can't be empty!";
			result = false;
		}
		else{
			$scope.loginFormValidation.usernameError = false;
		}
		//Password
		if($scope.loginForm.password == null || $scope.loginForm.password.length == 0){
			$scope.loginFormValidation.passwordError = true;
			$scope.loginFormValidation.passwordErrorMessage = "Password can't be empty!";
			result = false;
		}
		else{
			$scope.loginFormValidation.passwordError = false;
		}
		return result;
	}
});