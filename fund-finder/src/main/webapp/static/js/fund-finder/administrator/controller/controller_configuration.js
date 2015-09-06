angular.module('fundFinder')

.controller('Administrator_ConfigurationCtrl', function($rootScope, $scope, $stateParams, $state, ModalService, Administrator_ConfigurationService, Administrator_InvestmentService) {
	
	/** entity type (company/tender */
	$scope.entityType = ($state.current.data) ?$state.current.data.entityType : $stateParams.entityType;
	
	/** back (preview) */
	$scope.back = function() {
		$state.go('administrator.configuration_' + $scope.entityType);
	};
	
	/** get questions */
	$scope.getQuestions = function() {
		Administrator_ConfigurationService.getQuestions($scope.entityType)
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
		
	/** edit question (opens modal dialog) */
	$scope.editQuestion = function(questionId) {
		ModalService.showModal({
			templateUrl: "dialogs/editQuestion.html",
			controller: "Administrator_EditQuestionCtrl",
			inputs: {
				questionId: questionId,
				entityType: $scope.entityType
			}
		}).then(function(modal) {
			modal.element.modal();
		});
	}
	
	/** link question (opens modal dialog) */
	$scope.linkQuestion = function(questionId) {
		ModalService.showModal({
			templateUrl: "dialogs/linkQuestion.html",
			controller: "Administrator_LinkQuestionCtrl",
			inputs: {
				questionId: questionId,
				entityType: $scope.entityType
			}
		}).then(function(modal) {
			modal.element.modal();
		});
	}
	
	/** save question */
	$scope.saveQuestion = function(resource) {
		Administrator_ConfigurationService.saveQuestion(resource)
			.success(function(data, status, headers, config) {
				$scope.getQuestions();
			})
			.error(function(data, status, headers, config) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja stavke');
				}
			});
	}
	
	/** delete question */
	$scope.deleteQuestion = function(questionId) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: 'Obriši stavku',
            message: 'Da li doista želite obrisati stavku?',
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
	                	Administrator_ConfigurationService.deleteQuestion(questionId)
		        			.success(function(data, status, headers, config) {
		        				$scope.getQuestions();
		        				toastr.success('Stavka je uspješno obrisana');
		        			})
		        			.error(function(data, status, headers, config) {
		        				if (status == 403) {
		        					$state.go('login');
		        				} else {
		        					toastr.error('Došlo je do pogreške prilikom brisanja stavke');
		        				}
		        			});
	        			dialog.close();
	                }	                
            	}
            ]
        });
	};
	
	/** order changed callback handler */
	$scope.orderChanged = function() {
        angular.forEach($scope.questions, function(question, index) {
        	if (question.index != index) {
        		question.index = index;
        		Administrator_ConfigurationService.saveQuestion(question)
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

    /** get counties */
	Administrator_ConfigurationService.getCounties()
		.success(function(data, status, headers, config) {
			$scope.counties = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get NKDs */
	Administrator_ConfigurationService.getNkds()
		.success(function(data, status, headers, config) {
			$scope.nkds = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get investments */
	Administrator_InvestmentService.getInvestments()
		.success(function(data, status, headers, config) {
			$scope.investments = data;
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
		$state.go('administrator.configuration_preview', { 'entityType' : $scope.entityType });
	}
	
	/** text editor options */
	$scope.summernoteOptions = {
		height: 300,
		focus: false,
		airMode: false,
		toolbar: [['style', ['bold', 'italic', 'underline']], ['alignment', ['ul', 'ol', 'paragraph']], ['insert', ['link','hr']]]
	};
	
	// initial load
	$scope.getQuestions();
	
})

.controller('Administrator_EditQuestionCtrl', function($rootScope, $scope, $state, $element, Administrator_ConfigurationService, questionId, entityType) {
	
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
	
	/** get question metadata */
	Administrator_ConfigurationService.getQuestionMetadata()
		.success(function(data, status, headers, config) {
			$scope.questionMetadata = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get question */
	Administrator_ConfigurationService.getQuestion(entityType, questionId)
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
		Administrator_ConfigurationService.saveQuestion($scope.question)
			.success(function(data, status, headers, config) {
				$element.modal('hide');
				$state.go('administrator.configuration_' + entityType, {}, { reload : true });
				toastr.success('Stavka je uspješno spremljena');
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

.controller('Administrator_LinkQuestionCtrl', function($rootScope, $scope, $state, $element, Administrator_ConfigurationService, questionId, entityType) {
	
	/** get question */
	Administrator_ConfigurationService.getQuestion(entityType, questionId)
		.success(function(data, status, headers, config) {
			$scope.question = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
    /** get operators */
	Administrator_ConfigurationService.getOperators(questionId)
		.success(function(data, status, headers, config) {
			$scope.operators = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get questions */
	Administrator_ConfigurationService.getQuestions('company')
		.success(function(data, status, headers, config) {
			$scope.questions = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** save question */
	$scope.save = function() {
		var linkQuestionId = ($scope.question.linkQuestionId) ? $scope.question.linkQuestionId : "";
		var linkOperator = ($scope.question.linkOperator) ? $scope.question.linkOperator : "";
		
		Administrator_ConfigurationService.linkQuestion($scope.question.id, linkQuestionId, linkOperator)
			.success(function(data, status, headers, config) {
				$element.modal('hide');
				$state.go('administrator.configuration_' + entityType, {}, { reload : true });
				toastr.success('Podaci su uspješno spremljeni');
			})
			.error(function(data, status, headers, config) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
				}
			});
	}
	
})