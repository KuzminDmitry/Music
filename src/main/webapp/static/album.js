//Controller Part
app.controller("AlbumController", function($scope, $http, $rootScope, $cookies) {
	
	$scope.findTracksByAlbum = function(album){		
		$rootScope.albumId = album.id;
		$rootScope.$emit('findTracksByAlbum');
		$rootScope.activeTab = 0;
	}
	
    //Albums
    $rootScope.albums = [];

    $scope.addAlbumForm = {
        id : null,
        name : "",
        description : "",
		releaseDate : ""
    };

    //Now load the data from server
    _refreshAlbumData();


    $scope.insertAlbum = function() {
		if($scope.validateAddAlbumForm()){
			$http({
				method : "POST",
				url : 'rest/album',
				data : angular.toJson($scope.addAlbumForm),
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$rootScope.albums.unshift(response.data);
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		}
    };

    $scope.deleteAlbum = function(album) {
        $http({
            method : 'DELETE',
            url : 'rest/album/?id=' + album.id,
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback() {
            for (var i = 0; i < $rootScope.albums.length; i++)
                if ($rootScope.albums[i].id == album.id) {
                    $rootScope.albums.splice(i, 1);
                    break;
                }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.updateAlbum = function(album) {
        $http({
            method : "PUT",
            url : 'rest/album',
            data : angular.toJson(album),
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function _successAlbum(response) {
              for (var i = 0; i < $scope.albums.length; i++)
                if ($scope.albums[i].id == response.data.id) {
                    $scope.albums.splice(i, 1, response.data);
                    break;
                }
        },
        function _errorAlbum(response) {
            //log error
            console.log(response.statusText);
        });
    };

    /* Private Methods */
    function _refreshAlbumData() {
        $http({
            method : 'GET',
            url : 'rest/album',
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback(response) {
            $rootScope.albums = response.data;
        }, function errorCallback(response) {
			$cookies.remove("authdata"); 
			$rootScope.logged = false;
            console.log(response.statusText);
        });
    }
	
	$scope.addAlbumFormValidation = {
        nameError : false,		
		descriptionError : false,	
		nameErrorMessage: "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateAddAlbumForm = function(){
		var result = true;
		//Name
		if($scope.addAlbumForm.name == null || $scope.addAlbumForm.name.length == 0){
			$scope.addAlbumFormValidation.nameError = true;
			$scope.addAlbumFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.addAlbumFormValidation.nameError = false;
		}
		//Description
		if($scope.addAlbumForm.description == null || $scope.addAlbumForm.description.length == 0){
			$scope.addAlbumFormValidation.descriptionError = true;
			$scope.addAlbumFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.addAlbumFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.changeAdminModeStatus = function(){
		$scope.editAlbumFormValidation.nameError = false;
		$scope.editAlbumFormValidation.descriptionError = false;
		$rootScope.adminMode = !$rootScope.adminMode;
		if(!$scope.adminMode){
			for (var i = 0; i < $scope.albums.length; i++){
				if ($scope.albums[i].id == $scope.accordionNumber) {
					$scope.editAlbumForm.id = $scope.albums[i].id;
					$scope.editAlbumForm.name = $scope.albums[i].name;
					$scope.editAlbumForm.description = $scope.albums[i].description;
					$scope.editAlbumForm.releaseDate = $scope.albums[i].releaseDate;
					break;
				}
			}
		}				
	}
	
	
	
	$scope.accordionNumber = null;
	
	$scope.editAlbumForm = {
		id : null,
		name : "",
		description : ""
	}
	
	$scope.changeAccordionNumber = function(album) {
		if($scope.accordionNumber == album.id){
			$scope.accordionNumber = null;
		}
		else{
			$scope.accordionNumber = album.id;
		}
		$scope.editAlbumForm.id = album.id;
		$scope.editAlbumForm.name = album.name;
		$scope.editAlbumForm.description = album.description;
		$scope.editAlbumForm.releaseDate = album.releaseDate;
	}
	
	$scope.rollbackTrack = function(album){
		for (var i = 0; i < $scope.albums.length; i++){
			if ($scope.albums[i].id == album.id) {
				$scope.editAlbumForm.id = $scope.albums[i].id;
				$scope.editAlbumForm.name = $scope.albums[i].name;
				$scope.editAlbumForm.description = $scope.albums[i].description;
				$scope.editAlbumForm.releaseDate = $scope.albums[i].releaseDate;
				break;
			}
		}
		$scope.validateEditAlbumForm();
	}
	
	$scope.editAlbumFormValidation = {
        nameError : false,	
		descriptionError : false,
		nameErrorMessage : "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateEditAlbumForm = function(){
		var result = true;
		//Name
		if($scope.editAlbumForm.name == null || $scope.editAlbumForm.name.length == 0){
			$scope.editAlbumFormValidation.nameError = true;
			$scope.editAlbumFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.editAlbumFormValidation.nameError = false;
		}
		//Description
		if($scope.editAlbumForm.description == null || $scope.editAlbumForm.description.length == 0){
			$scope.editAlbumFormValidation.descriptionError = true;
			$scope.editAlbumFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.editAlbumFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.rollbackAlbum = function(album){
		for (var i = 0; i < $scope.albums.length; i++){
			if ($scope.albums[i].id == album.id) {
				$scope.editAlbumForm.id = $scope.albums[i].id;
				$scope.editAlbumForm.name = $scope.albums[i].name;
				$scope.editAlbumForm.description = $scope.albums[i].description;
				$scope.editAlbumForm.releaseDate = $scope.albums[i].releaseDate;
				break;
			}
		}
		$scope.validateEditAlbumForm();
	}
	
	$scope.addFormTodayReleaseDate = function() {
		$scope.addAlbumForm.releaseDate = new Date();
	};
	
	$scope.addFormTodayReleaseDate();

	$scope.clear = function() {
		$scope.addAlbumForm.releaseDate = null;
	};

	$scope.addAlbumFormDateOptions = {
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
		$scope.addAlbumForm.releaseDate = new Date(year, month, day);
		
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