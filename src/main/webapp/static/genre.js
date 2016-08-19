//Controller Part
app.controller("GenreController", function($scope, $http, $rootScope, $cookies) {
	
	$scope.findTracksByGenre = function(genre){		
		$rootScope.genreId = genre.id;
		$rootScope.$emit('findTracksByGenre');
		$rootScope.activeTab = 0;
	}
	
    //Genres
    $rootScope.genres = [];

    $scope.addGenreForm = {
        id : null,
        name : "",
        description : ""
    };

    //Now load the data from server
    _refreshGenreData();


    $scope.insertGenre = function() {
		if($scope.validateAddGenreForm()){
			$http({
				method : "POST",
				url : 'rest/genre',
				data : angular.toJson($scope.addGenreForm),
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$rootScope.genres.unshift(response.data);
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		}
    };

    $scope.deleteGenre = function(genre) {
        $http({
            method : 'DELETE',
            url : 'rest/genre/?id=' + genre.id,
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback() {
            for (var i = 0; i < $rootScope.genres.length; i++)
                if ($rootScope.genres[i].id == genre.id) {
                    $rootScope.genres.splice(i, 1);
                    break;
                }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.updateGenre = function(genre) {
        $http({
            method : "PUT",
            url : 'rest/genre',
            data : angular.toJson(genre),
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function _successGenre(response) {
              for (var i = 0; i < $scope.genres.length; i++)
                if ($scope.genres[i].id == response.data.id) {
                    $scope.genres.splice(i, 1, response.data);
                    break;
                }
        },
        function _errorGenre(response) {
            //log error
            console.log(response.statusText);
        });
    };

    /* Private Methods */
    function _refreshGenreData() {
        $http({
            method : 'GET',
            url : 'rest/genre',
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback(response) {
            $rootScope.genres = response.data;
        }, function errorCallback(response) {
			$cookies.remove("authdata"); 
			$rootScope.logged = false;
            console.log(response.statusText);
        });
    }
	
	$scope.addGenreFormValidation = {
        nameError : false,		
		descriptionError : false,	
		nameErrorMessage: "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateAddGenreForm = function(){
		var result = true;
		//Name
		if($scope.addGenreForm.name == null || $scope.addGenreForm.name.length == 0){
			$scope.addGenreFormValidation.nameError = true;
			$scope.addGenreFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.addGenreFormValidation.nameError = false;
		}
		//Description
		if($scope.addGenreForm.description == null || $scope.addGenreForm.description.length == 0){
			$scope.addGenreFormValidation.descriptionError = true;
			$scope.addGenreFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.addGenreFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.changeAdminModeStatus = function(){
		$scope.editGenreFormValidation.nameError = false;
		$scope.editGenreFormValidation.descriptionError = false;
		$rootScope.adminMode = !$rootScope.adminMode;
		if(!$scope.adminMode){
			for (var i = 0; i < $scope.genres.length; i++){
				if ($scope.genres[i].id == $scope.accordionNumber) {
					$scope.editGenreForm.id = $scope.genres[i].id;
					$scope.editGenreForm.name = $scope.genres[i].name;
					$scope.editGenreForm.description = $scope.genres[i].description;
					break;
				}
			}
		}				
	}
	
	
	
	$scope.accordionNumber = null;
	
	$scope.editGenreForm = {
		id : null,
		name : "",
		description : ""
	}
	
	$scope.changeAccordionNumber = function(genre) {
		if($scope.accordionNumber == genre.id){
			$scope.accordionNumber = null;
		}
		else{
			$scope.accordionNumber = genre.id;
		}
		$scope.editGenreForm.id = genre.id;
		$scope.editGenreForm.name = genre.name;
		$scope.editGenreForm.description = genre.description;
	}
	
	$scope.rollbackTrack = function(genre){
		for (var i = 0; i < $scope.genres.length; i++){
			if ($scope.genres[i].id == genre.id) {
				$scope.editGenreForm.id = $scope.genres[i].id;
				$scope.editGenreForm.name = $scope.genres[i].name;
				$scope.editGenreForm.description = $scope.genres[i].description;
				break;
			}
		}
		$scope.validateEditGenreForm();
	}
	
	$scope.editGenreFormValidation = {
        nameError : false,	
		descriptionError : false,
		nameErrorMessage : "",
		descriptionErrorMessage : ""
    };
	
	$scope.validateEditGenreForm = function(){
		var result = true;
		//Name
		if($scope.editGenreForm.name == null || $scope.editGenreForm.name.length == 0){
			$scope.editGenreFormValidation.nameError = true;
			$scope.editGenreFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.editGenreFormValidation.nameError = false;
		}
		//Description
		if($scope.editGenreForm.description == null || $scope.editGenreForm.description.length == 0){
			$scope.editGenreFormValidation.descriptionError = true;
			$scope.editGenreFormValidation.descriptionErrorMessage = "Description can't be empty!";
			result = false;
		}
		else{
			$scope.editGenreFormValidation.descriptionError = false;
		}
		return result;
	}
	
	$scope.rollbackGenre = function(genre){
		for (var i = 0; i < $scope.genres.length; i++){
			if ($scope.genres[i].id == genre.id) {
				$scope.editGenreForm.id = $scope.genres[i].id;
				$scope.editGenreForm.name = $scope.genres[i].name;
				$scope.editGenreForm.description = $scope.genres[i].description;
				break;
			}
		}
		$scope.validateEditGenreForm();
	}
});