//Controller Part
app.controller("SingerController", function($scope, $http, $rootScope, $cookies) {
	
	$scope.findTracksBySinger = function(singer){		
		$rootScope.singerId = singer.id;
		$rootScope.$emit('findTracksBySinger');
		$rootScope.activeTab = 0;
	}
	
    //Singers
    $rootScope.singers = [];

    $scope.addSingerForm = {
        id : null,
        name : "",
        description : "",
		birthDate : ""
    };

    //Now load the data from server
    _refreshSingerData();


    $scope.insertSinger = function() {
		if($scope.validateAddSingerForm()){
			$http({
				method : "POST",
				url : 'rest/singer',
				data : angular.toJson($scope.addSingerForm),
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$rootScope.singers.unshift(response.data);
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		}
    };

    $scope.deleteSinger = function(singer) {
        $http({
            method : 'DELETE',
            url : 'rest/singer/?id=' + singer.id,
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback() {
            for (var i = 0; i < $rootScope.singers.length; i++)
                if ($rootScope.singers[i].id == singer.id) {
                    $rootScope.singers.splice(i, 1);
                    break;
                }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.updateSinger = function(singer) {
        $http({
            method : "PUT",
            url : 'rest/singer',
            data : angular.toJson(singer),
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function _successSinger(response) {
              for (var i = 0; i < $scope.singers.length; i++)
                if ($scope.singers[i].id == response.data.id) {
                    $scope.singers.splice(i, 1, response.data);
                    break;
                }
        },
        function _errorSinger(response) {
            //log error
            console.log(response.statusText);
        });
    };

    /* Private Methods */
    function _refreshSingerData() {
        $http({
            method : 'GET',
            url : 'rest/singer',
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback(response) {
            $rootScope.singers = response.data;
        }, function errorCallback(response) {
			$cookies.remove("authdata"); 
			$rootScope.logged = false;
            console.log(response.statusText);
        });
    }
	
	$scope.addSingerFormValidation = {
        nameError : false,		
		descriptionError : false,	
		nameErrorMessage: "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateAddSingerForm = function(){
		var result = true;
		//Name
		if($scope.addSingerForm.name == null || $scope.addSingerForm.name.length == 0){
			$scope.addSingerFormValidation.nameError = true;
			$scope.addSingerFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.addSingerFormValidation.nameError = false;
		}
		//Description
		if($scope.addSingerForm.description == null || $scope.addSingerForm.description.length == 0){
			$scope.addSingerFormValidation.descriptionError = true;
			$scope.addSingerFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.addSingerFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.changeAdminModeStatus = function(){
		$scope.editSingerFormValidation.nameError = false;
		$scope.editSingerFormValidation.descriptionError = false;
		$rootScope.adminMode = !$rootScope.adminMode;
		if(!$scope.adminMode){
			for (var i = 0; i < $scope.singers.length; i++){
				if ($scope.singers[i].id == $scope.accordionNumber) {
					$scope.editSingerForm.id = $scope.singers[i].id;
					$scope.editSingerForm.name = $scope.singers[i].name;
					$scope.editSingerForm.description = $scope.singers[i].description;
					$scope.editSingerForm.birthDate = $scope.singers[i].birthDate;
					break;
				}
			}
		}				
	}
	
	
	
	$scope.accordionNumber = null;
	
	$scope.editSingerForm = {
		id : null,
		name : "",
		description : ""
	}
	
	$scope.changeAccordionNumber = function(singer) {
		if($scope.accordionNumber == singer.id){
			$scope.accordionNumber = null;
		}
		else{
			$scope.accordionNumber = singer.id;
		}
		$scope.editSingerForm.id = singer.id;
		$scope.editSingerForm.name = singer.name;
		$scope.editSingerForm.description = singer.description;
		$scope.editSingerForm.birthDate = singer.birthDate;
	}
	
	$scope.rollbackTrack = function(singer){
		for (var i = 0; i < $scope.singers.length; i++){
			if ($scope.singers[i].id == singer.id) {
				$scope.editSingerForm.id = $scope.singers[i].id;
				$scope.editSingerForm.name = $scope.singers[i].name;
				$scope.editSingerForm.description = $scope.singers[i].description;
				$scope.editSingerForm.birthDate = $scope.singers[i].birthDate;
				break;
			}
		}
		$scope.validateEditSingerForm();
	}
	
	$scope.editSingerFormValidation = {
        nameError : false,	
		descriptionError : false,
		nameErrorMessage : "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateEditSingerForm = function(){
		var result = true;
		//Name
		if($scope.editSingerForm.name == null || $scope.editSingerForm.name.length == 0){
			$scope.editSingerFormValidation.nameError = true;
			$scope.editSingerFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.editSingerFormValidation.nameError = false;
		}
		//Description
		if($scope.editSingerForm.description == null || $scope.editSingerForm.description.length == 0){
			$scope.editSingerFormValidation.descriptionError = true;
			$scope.editSingerFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.editSingerFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.rollbackSinger = function(singer){
		for (var i = 0; i < $scope.singers.length; i++){
			if ($scope.singers[i].id == singer.id) {
				$scope.editSingerForm.id = $scope.singers[i].id;
				$scope.editSingerForm.name = $scope.singers[i].name;
				$scope.editSingerForm.description = $scope.singers[i].description;
				$scope.editSingerForm.birthDate = $scope.singers[i].birthDate;
				break;
			}
		}
		$scope.validateEditSingerForm();
	}
	
	$scope.addFormTodayReleaseDate = function() {
		$scope.addSingerForm.birthDate = new Date();
	};
	
	$scope.addFormTodayReleaseDate();

	$scope.clear = function() {
		$scope.addSingerForm.birthDate = null;
	};

	$scope.addSingerFormDateOptions = {
		customClass: getDayClass,
		showWeeks: true
	};

	// Disable weekend selection
	function disabled(data) {
		var date = data.date,
		mode = data.mode;
		return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
	}


	

	$scope.setDate = function(year, month, day) {
		$scope.addSingerForm.birthDate = new Date(year, month, day);
		
	};


	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	var afterTomorrow = new Date(tomorrow);
	afterTomorrow.setDate(tomorrow.getDate() + 1);

	$scope.events = [
		{
			date: tomorrow,
			status: 'full'
		},
		{
			date: afterTomorrow,
			status: 'partially'
		}
	];

	function getDayClass(data) {
		var date = data.date,
		mode = data.mode;
		if (mode === 'day') {
			var dayToCheck = new Date(date).setHours(0,0,0,0);

			for (var i = 0; i < $scope.events.length; i++) {
				var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

				if (dayToCheck === currentDay) {
					return $scope.events[i].status;
				}
			}
		}
		return '';
	}
});