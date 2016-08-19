var app = angular.module("MusicManagement", ['ui.bootstrap', 'ui.select', 'ngCookies']);

//Controller Part
app.controller("MusicController", function($scope, $http, $rootScope) {

    //Labels
    $rootScope.activeTab = 0;
	
	$scope.selectTab = function(value) {
        $rootScope.activeTab = value;
    };
	
	$rootScope.cookieEnabled = true;
	
	areCookiesEnabled();
	
	function areCookiesEnabled()
	{
		var cookieEnabled = (navigator.cookieEnabled) ? true : false;

		if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
		{ 
			document.cookie="testcookie";
			cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
		}
		$rootScope.cookieEnabled = cookieEnabled;
	}
});