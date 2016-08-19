//Controller Part
app.controller("LabelController", function($scope, $http, $rootScope, $cookies) {
	
	$scope.findTracksByLabel = function(label){		
		$rootScope.labelId = label.id;
		$rootScope.$emit('findTracksByLabel');
		$rootScope.activeTab = 0;
	}
	
    //Labels
    $rootScope.labels = [];

    $scope.addLabelForm = {
        id : null,
        name : ""
    };

    //Now load the data from server
    _refreshLabelData();


    $scope.insertLabel = function() {
		if($scope.validateAddLabelForm()){
			$http({
				method : "POST",
				url : 'rest/label',
				data : angular.toJson($scope.addLabelForm),
				headers : {
					'Content-Type' : 'application/json; charset=UTF-8'
				}
			}).then(function successCallback(response) {
				$rootScope.labels.unshift(response.data);
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		}
    };

    $scope.deleteLabel = function(label) {
        $http({
            method : 'DELETE',
            url : 'rest/label/?id=' + label.id,
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback() {
            for (var i = 0; i < $rootScope.labels.length; i++)
                if ($rootScope.labels[i].id == label.id) {
                    $rootScope.labels.splice(i, 1);
                    break;
                }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.updateLabel = function(label) {
        $http({
            method : "PUT",
            url : 'rest/label',
            data : angular.toJson(label),
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function _successLabel(response) {
              for (var i = 0; i < $scope.labels.length; i++)
                if ($scope.labels[i].id == response.data.id) {
                    $scope.labels.splice(i, 1, response.data);
                    break;
                }
        },
        function _errorLabel(response) {
            //log error
            console.log(response.statusText);
        });
    };

    /* Private Methods */
    function _refreshLabelData() {
        $http({
            method : 'GET',
            url : 'rest/label',
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(function successCallback(response) {
            $rootScope.labels = response.data;
        }, function errorCallback(response) {
			$cookies.remove("authdata"); 
			$rootScope.logged = false;
            console.log(response.statusText);
        });
    }
	
	$scope.addLabelFormValidation = {
        nameError : false,			
		nameErrorMessage: ""
    };
	
	$scope.validateAddLabelForm = function(){
		var result = true;
		//Name
		if($scope.addLabelForm.name == null || $scope.addLabelForm.name.length == 0){
			$scope.addLabelFormValidation.nameError = true;
			$scope.addLabelFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.addLabelFormValidation.nameError = false;
		}
		return result;
	}
	
	$scope.changeAdminModeStatus = function(){
		$scope.editLabelFormValidation.nameError = false;
		$rootScope.adminMode = !$rootScope.adminMode;
		if(!$scope.adminMode){
			for (var i = 0; i < $scope.labels.length; i++){
				if ($scope.labels[i].id == $scope.accordionNumber) {
					$scope.editLabelForm.id = $scope.labels[i].id;
					$scope.editLabelForm.name = $scope.labels[i].name;
					break;
				}
			}
		}				
	}
	
	
	
	$scope.accordionNumber = null;
	
	$scope.editLabelForm = {
		id : null,
		name : ""
	}
	
	$scope.changeAccordionNumber = function(label) {
		if($scope.accordionNumber == label.id){
			$scope.accordionNumber = null;
		}
		else{
			$scope.accordionNumber = label.id;
		}
		$scope.editLabelForm.id = label.id;
		$scope.editLabelForm.name = label.name;
	}
	
	$scope.rollbackTrack = function(label){
		for (var i = 0; i < $scope.labels.length; i++){
			if ($scope.labels[i].id == label.id) {
				$scope.editLabelForm.id = $scope.labels[i].id;
				$scope.editLabelForm.name = $scope.labels[i].name;
				break;
			}
		}
		$scope.validateEditLabelForm();
	}
	
	$scope.editLabelFormValidation = {
        nameError : false,	
		nameErrorMessage : ""
    };
	
	$scope.validateEditLabelForm = function(){
		var result = true;
		//Name
		if($scope.editLabelForm.name == null || $scope.editLabelForm.name.length == 0){
			$scope.editLabelFormValidation.nameError = true;
			$scope.editLabelFormValidation.nameErrorMessage = "Name can't be empty!";
			result = false;
		}
		else{
			$scope.editLabelFormValidation.nameError = false;
		}
		return result;
	}
	
	$scope.rollbackLabel = function(label){
		for (var i = 0; i < $scope.labels.length; i++){
			if ($scope.labels[i].id == label.id) {
				$scope.editLabelForm.id = $scope.labels[i].id;
				$scope.editLabelForm.name = $scope.labels[i].name;
				break;
			}
		}
		$scope.validateEditLabelForm();
	}
});