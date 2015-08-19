angular.module('fundFinder')

.controller('Administrator_ConfigurationCompanyCtrl', function($rootScope, $scope, $state, ModalService, Administrator_ConfigurationService) {
	
	$scope.back = function() {
		$state.go('administrator.configuration_company');
	};
	
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
		
	$scope.editQuestion = function(questionId) {
		ModalService.showModal({
			templateUrl: "dialogs/editCompanyQuestion.html",
			controller: "Administrator_EditCompanyQuestionCtrl",
			inputs: {
				questionId: questionId
			}
		}).then(function(modal) {
			modal.element.modal();
		});
	}
	
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
	
	$scope.deleteQuestion = function(questionId) {
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
	                	Administrator_ConfigurationService.deleteCompanyQuestion(questionId)
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
	
	$scope.orderChanged = function() {
        angular.forEach($scope.questions, function(question, index) {
        	if (question.index != index) {
        		question.index = index;
        		Administrator_ConfigurationService.saveCompanyQuestion(question)
	    			.success(function(data, status, headers, config) {
	    				question = data;
	    				toastr.success('Promjene su uspješno spremljene');
	    			})
	    			.error(function(data, status, headers, config) {
	    				if (status == 403) {
	    					$state.go('login');
	    				} else {
	    					toastr.error('Došlo je do pogreške prilikom spremanja podataka');
	    				}
	    			});
        	}
        });
    };
    
    /** get cities */
	Administrator_ConfigurationService.getCities()
		.success(function(data, status, headers, config) {
			$scope.cities = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get cities */
	Administrator_ConfigurationService.getNkds()
		.success(function(data, status, headers, config) {
			
			var nkdSectors = new Array();
			angular.forEach(data, function(nkd, index) {
				var nkdSector = nkd.sector + " - " + nkd.sectorName;
				if (nkdSectors.indexOf(nkdSector) == -1) {
					nkdSectors.push(nkdSector);
				}
			});
			
			$scope.nkdSectors = nkdSectors;
			
			$scope.nkds = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
    /** preview */
	$scope.preview = function() {
		$state.go('administrator.configuration_company_preview');
	}
	
	$scope.summernoteOptions = {
		height: 300,
		focus: false,
		airMode: false,
		toolbar: [['style', ['bold', 'italic', 'underline']], ['alignment', ['ul', 'ol', 'paragraph']], ['insert', ['link','picture','hr']]]
	};
	
	// initial load
	$scope.getCompanyQuestions();
	
})

.controller('Administrator_EditCompanyQuestionCtrl', function($rootScope, $scope, $state, $element, Administrator_ConfigurationService, questionId) {
	
	/** get question types */
	Administrator_ConfigurationService.getQuestionTypes()
		.success(function(data, status, headers, config) {
			$scope.questionTypes = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get question */
	Administrator_ConfigurationService.getCompanyQuestion(questionId)
		.success(function(data, status, headers, config) {
			if (data.id) {
				$scope.isEditMode = true;
			} else {
				$scope.isEditMode = false;
			}
			$scope.question = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** add option */
	$scope.addOption = function(index) {
		$scope.question.options.splice(index + 1, 0, { value : "" })
	}
	
	/** remove option */
	$scope.removeOption = function(index) {
		$scope.question.options.splice(index, 1);
	}
	
	/** save question */
	$scope.save = function() {
		Administrator_ConfigurationService.saveCompanyQuestion($scope.question)
			.success(function(data, status, headers, config) {
				$element.modal('hide');
				$state.go('administrator.configuration_company', {}, { reload : true });
				toastr.success('Pitanje je uspješno spremljeno');
			})
			.error(function(data, status, headers, config) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja podataka');
				}
			});
	}
	
})

.controller('Administrator_ConfigurationTenderCtrl', function($rootScope, $scope, $state) {

})