angular.module('fundFinder')

.controller('Administrator_ConfigurationCompanyCtrl', function($rootScope, $scope, $state, Administrator_ConfigurationService) {
	
	$scope.getCompanyQuestions = function() {
		Administrator_ConfigurationService.getCompanyQuestions()
			.success(function(data, status, headers, config) {
				$scope.selected = null;
				$scope.questions = data;
			})
			.error(function(data, status, headers, config) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
				}
			});
	}
		
	$scope.addQuestion = function() {
		$scope.saveQuestion({
			entityType: "COMPANY",
			index: $scope.questions.length,
			text: "Pravni oblik",
			type: "TEXT",
			options: []
		});
	}
	
	$scope.editQuestion = function(index) {
		
	};
	
	$scope.saveQuestion = function(resource) {
		Administrator_ConfigurationService.saveCompanyQuestion(resource)
			.success(function(data, status, headers, config) {
				$scope.getCompanyQuestions();
			})
			.error(function(data, status, headers, config) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja pitanja');
				}
			});
	}
	
	$scope.deleteQuestion = function(question) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: 'Obriši pitanje',
            message: 'Da li doista želite obrisati pitanje?',
            buttons: [
				{
					label: 'Ne',
					icon: 'fa fa-times',
				    cssClass: 'btn-white',
				    action: function(dialog) {
				        dialog.close();
				    }
				},
            	{
            		label: 'Da',
	            	icon: 'fa fa-check',
	                cssClass: 'btn-primary',
	                action: function(dialog) {
	                	Administrator_ConfigurationService.deleteCompanyQuestion(question.id)
		        			.success(function(data, status, headers, config) {
		        				$scope.getCompanyQuestions();
		        				toastr.success('Pitanje je uspješno obrisano');
		        			})
		        			.error(function(data, status, headers, config) {
		        				if (status == 403) {
		        					$state.go('login');
		        				} else {
		        					toastr.error('Došlo je do pogreške prilikom brisanja pitanja');
		        				}
		        			});
	        			dialog.close();
	                }	                
            	}
            ]
        });
	};
	
	// initial load
	$scope.getCompanyQuestions();
	
})

.controller('Administrator_ConfigurationTenderCtrl', function($rootScope, $scope, $state) {

})