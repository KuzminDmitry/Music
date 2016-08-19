//Controller Part
app.controller("TrackController", function($scope, $http, $rootScope, $filter){
	
	

	
    //Tracks
    $scope.tracks = [];
	
	$scope.searchForm = {
		genreSelect : null,
		labelSelect : null,
		albumSelect : null,
		singerSelect : null
	}
	
	$scope.refreshSearchFrom = function(){
		$scope.searchForm.genreSelect = null,
		$scope.searchForm.labelSelect = null,
		$scope.searchForm.albumSelect = null,
		$scope.searchForm.singerSelect = null
	}

    $scope.addTrackForm = {
        id : null,
        name : null,
		releaseDate : null,		
		singerIds : null,
		albumId : null,			
		labelId : null,		
		genreId : null,
    };

	$rootScope.$on('findTracksByGenre', function() {
		if($rootScope.genreId != null){
			$http({
				method : 'GET',
				url : 'rest/track/find?genreId='+$rootScope.genreId,
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$scope.tracks = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});		
		}
	});
	
	$rootScope.$on('findTracksByLabel', function() {
		if($rootScope.labelId != null){
			$http({
				method : 'GET',
				url : 'rest/track/find?labelId='+$rootScope.labelId,
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$scope.tracks = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});		
		}
	});
	
	$rootScope.$on('findTracksByAlbum', function() {
		if($rootScope.albumId != null){
			$http({
				method : 'GET',
				url : 'rest/track/find?albumId='+$rootScope.albumId,
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$scope.tracks = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});		
		}
	});
	
	$rootScope.$on('findTracksBySinger', function() {
		if($rootScope.singerId != null){
			$http({
				method : 'GET',
				url : 'rest/track/find?singerId='+$rootScope.singerId,
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$scope.tracks = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});		
		}
	});
	
    //Now load the data from server
    //_refreshTrackData();

  $scope.insertTrack = function() {
		//if($scope.validateAddTrackForm()){
			$http({
				method : "POST",
				url : 'rest/track',
				data : angular.toJson($scope.addTrackForm),
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				var releaseDate = new Date(response.data.releaseDate);
				response.data.releaseDate = $filter("date")(releaseDate, "yyyy-MM-dd");
				$scope.tracks.unshift(response.data);
			}, function errorCallback(response) {
				console.log(response.statusText);
				 console.log(angular.toJson($scope.addTrackForm));
			});
		//}
    };

    $scope.deleteTrack = function(track) {
        $http({
            method : 'DELETE',
            url : 'rest/track/?id=' + track.id,
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback() {
            for (var i = 0; i < $scope.tracks.length; i++)
                if ($scope.tracks[i].id == track.id) {
                    $scope.tracks.splice(i, 1);
                    break;
                }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };
	


    $scope.updateTrack = function(track) {
		if(track.albumId==0) track.albumId = null;
        $http({
            method : "PUT",
            url : 'rest/track',
            data : angular.toJson(track),
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function _successTrack(response) {
             for (var i = 0; i < $scope.tracks.length; i++)
                if ($scope.tracks[i].id == response.data.id) {
                    $scope.tracks.splice(i, 1, response.data);
                    break;
                }
        },
        function _errorTrack(response) {
            //log error
            console.log(response.statusText);
        });
    };

    /* Private Methods */
    $scope.refreshTracks = function() {	
		var url = "rest/track/find?";
		var check = false;
		if($scope.searchForm.genreSelect!=null){
			if(check){
				url +="&";
			}
			url += "genreId=" + $scope.searchForm.genreSelect;
			check = true;
		}
		if($scope.searchForm.labelSelect!=null){
			if(check){
				url +="&";
			}
			url += "labelId=" + $scope.searchForm.labelSelect;
			check = true;
		}
		if($scope.searchForm.albumSelect!=null){
			if(check){
				url +="&";
			}
			url += "albumId=" + $scope.searchForm.albumSelect;
			check = true;
		}
		if($scope.searchForm.singerSelect!=null){
			if(check){
				url +="&";
			}
			url += "singerId=" + $scope.searchForm.singerSelect;
			check = true;
		}
        $http({
            method : "GET",
            url : url,
            headers : {
                "Content-Type" : "application/json; charset=UTF-8"
            }
        }).then(function successCallback(response) {
            $scope.tracks = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
	
	
	$scope.addFormTodayReleaseDate = function() {
		$scope.addTrackForm.releaseDate = new Date();
	};
	
	$scope.addFormTodayReleaseDate();

	$scope.clear = function() {
		$scope.addTrackForm.releaseDate = null;
	};

	$scope.addTrackFormDateOptions = {
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
		$scope.addTrackForm.releaseDate = new Date(year, month, day);
		
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
	
	$scope.showAddTrackForm = false;
	
	$scope.changeAddTrackFormStatus = function(){
		$scope.showAddTrackForm = !$scope.showAddTrackForm;
	}
	
	$rootScope.adminMode = false;
	
	$scope.changeAdminModeStatus = function(){
		$scope.editTrackFormValidation.nameError = false;
		$scope.editTrackFormValidation.singerIdsError = false;
		$rootScope.adminMode = !$rootScope.adminMode;
		if(!$scope.adminMode){
			for (var i = 0; i < $scope.tracks.length; i++){
				if ($scope.tracks[i].id == $scope.accordionNumber) {
					$scope.editTrackForm.id = $scope.tracks[i].id;
					$scope.editTrackForm.name = $scope.tracks[i].name;
					$scope.editTrackForm.releaseDate = $scope.tracks[i].releaseDate;
					$scope.editTrackForm.genreId = $scope.tracks[i].genreId;
					$scope.editTrackForm.labelId = $scope.tracks[i].labelId;
					$scope.editTrackForm.albumId = $scope.tracks[i].albumId;
					$scope.editTrackForm.singerIds = $scope.tracks[i].singerIds;
					break;
				}
			}
		}		
	}

	
	$scope.addTrackFormValidation = {
        nameError : false,		
		genreIdError : false,			
		labelIdError : false,	
		albumIdError: false,
		singerIdsError : false,
		nameErrorMessage : "",
		genreIdErrorMessage : "",
		labelIdErrorMessage : "",
		albumIdErrorMessage: "",
		singerIdsErrorMessage : ""
    };
	
	
	
	$scope.validateAddTrackForm = function(){
		var result = true;
		//Name
		if($scope.addTrackForm.name == null || $scope.addTrackForm.name.length == 0){
			$scope.addTrackFormValidation.nameError = true;
			$scope.addTrackFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.addTrackFormValidation.nameError = false;
		}
		//Genre
		if($scope.addTrackForm.genreId == null){
			$scope.addTrackFormValidation.genreIdError = true;
			$scope.addTrackFormValidation.genreIdErrorMessage = "Genre must be selected!";
			result = false;
		}
		else{
			$scope.addTrackFormValidation.genreIdError = false;
		}
		//Label
		if($scope.addTrackForm.labelId == null){
			$scope.addTrackFormValidation.labelIdError = true;
			$scope.addTrackFormValidation.labelIdErrorMessage = "Label must be selected!";
			result = false;
		}
		else{
			$scope.addTrackFormValidation.labelIdError = false;
		}
		//Singers
		if($scope.addTrackForm.singerIds == null || $scope.addTrackForm.singerIds.length == 0){
			$scope.addTrackFormValidation.singerIdsError = true;
			$scope.addTrackFormValidation.singerIdsErrorMessage = "Singers must be selected!";
			result = false;
		}
		else{
			$scope.addTrackFormValidation.singerIdsError = false;
		}
		return result;
	}
	
	$scope.editFormTodayReleaseDate = function() {		
		$scope.editTrackForm.releaseDate = $filter("date")(new Date(), "yyyy-MM-dd");      
	};
	
	$scope.accordionNumber = null;
	
	$scope.accordionTrack = function(track) {
		if($scope.accordionNumber == track.id){
			$scope.accordionNumber = null;
		}
		else{
			$scope.accordionNumber = track.id;
		}
		$scope.editTrackForm.id = track.id;
		$scope.editTrackForm.name = track.name;
		$scope.editTrackForm.releaseDate = track.releaseDate;
		$scope.editTrackForm.genreId = track.genreId;
		$scope.editTrackForm.labelId = track.labelId;
		$scope.editTrackForm.albumId = track.albumId;
		$scope.editTrackForm.singerIds = track.singerIds;
	}
	
	
	$scope.editTrackForm = {
        id : null,
        name : null,
		releaseDate : null,		
		singerIds : null,
		albumId : null,			
		labelId : null,		
		genreId : null,
    };
	
	$scope.rollbackTrack = function(track){
		for (var i = 0; i < $scope.tracks.length; i++){
			if ($scope.tracks[i].id == track.id) {
				$scope.editTrackForm.id = $scope.tracks[i].id;
				$scope.editTrackForm.name = $scope.tracks[i].name;
				$scope.editTrackForm.releaseDate = $scope.tracks[i].releaseDate;
				$scope.editTrackForm.genreId = $scope.tracks[i].genreId;
				$scope.editTrackForm.labelId = $scope.tracks[i].labelId;
				$scope.editTrackForm.albumId = $scope.tracks[i].albumId;
				$scope.editTrackForm.singerIds = $scope.tracks[i].singerIds;
				break;
			}
		}
		$scope.validateEditTrackForm();
	}
	
	$scope.editTrackFormValidation = {
        nameError : false,	
		singerIdsError : false,
		nameErrorMessage : "",
		singerIdsErrorMessage : ""
    };	
	
	$scope.validateEditTrackForm = function(){
		var result = true;
		//Name
		if($scope.editTrackForm.name == null || $scope.editTrackForm.name.length == 0){
			$scope.editTrackFormValidation.nameError = true;
			$scope.editTrackFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.editTrackFormValidation.nameError = false;
		}
		//Singers
		if($scope.editTrackForm.singerIds == null || $scope.editTrackForm.singerIds.length == 0){
			$scope.editTrackFormValidation.singerIdsError = true;
			$scope.editTrackFormValidation.singerIdsErrorMessage = "Singers must be selected!";
			result = false;
		}
		else{
			$scope.editTrackFormValidation.singerIdsError = false;
		}
		return result;
	}
});