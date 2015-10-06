angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('Administrator_TenderOverviewCtrl', function($rootScope, $scope, $state, Administrator_TenderService) {
	$scope.gridOptions = {
			enableScrollbars: false,
			paginationPageSizes: [10, 20, 30, 50, 100],
			paginationPageSize: 10,
			useExternalPagination: true,
			enableFiltering: true,
			useExternalFiltering: true,
			totalItems: 10,
			rowHeight: 32,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 0,
			enableColumnMenus: false,
			enableGridMenu: false,
			columnDefs: [
				{
					name: 'Naziv',
					field: 'name',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					cellTemplate:'<div class="ui-grid-cell-contents"><a class="custom-a" ng-click="grid.appScope.showTender(row.entity)">{{row.entity.name}}</a></div>'
				},
				{
					name: 'Status',
					field: 'active',
					type: 'boolean',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					width: 100,
					cellTemplate:'<div ng-show="row.entity.active == true" class="ui-grid-cell-contents"><span class="badge badge-primary"><small>AKTIVAN</small></span></div><div ng-show="row.entity.active == false" class="ui-grid-cell-contents"><span class="badge badge-danger"><small>NEAKTIVAN</small></span></div>'
				},
				{
					name: 'Kreiran',
					field: 'timeCreated',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					width: 150
				},
				{
					name: 'Akcija',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: 126,
					cellTemplate:
						'<div style="padding-top: 1px">' +
						'<button ng-show="row.entity.active == false" ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.activateTender(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
						'<button ng-show="row.entity.active == true" ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.deactivateTender(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-on"></i></button>' +
						'<button ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.editTender(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-edit"></i></button>' +
						'<button ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.deleteTender(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-times"></i></button>' + 
						'</div>'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				
				// register pagination changed handler
				$scope.gridApi.pagination.on.paginationChanged($scope, function(currentPage, pageSize) {
					$scope.getPage(currentPage, pageSize);
				});
				
				// register sort changed handler 
				$scope.gridApi.core.on.sortChanged($scope, $scope.sortChanged);
				
				// register filter changed handler
				$scope.gridApi.core.on.filterChanged($scope, function() {
					var grid = this.grid;
					
					var filterArray = new Array();
					for (var i=0; i<grid.columns.length; i++) {
						var name = grid.columns[i].field;
						var term = grid.columns[i].filters[0].term;
						if (name && term) {
							filterArray.push({
								"name" : name,
								"term" : term
							});
						}
					}
					
					$scope.filterArray = filterArray;
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				});
			}
		};
    	
    	$scope.getPage = function(page, size) {
			var uiGridResource = {
				"pagination" : {
					"page" : page - 1,
					"size" : size
				},
				"sort" : $scope.sortArray,
				"filter" : $scope.filterArray
			};
			
			Administrator_TenderService.getPage(uiGridResource).
				success(function(data, status, headers, config) {
					$scope.gridOptions.data = data.data;
					$scope.gridOptions.totalItems = data.total;

					var newHeight = data.data.length * 32 + (($scope.gridOptions.totalItems == 0) ? 90 : 90);
					angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
				}).
				error(function(data, status, headers, config) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
					}
				});
		};
		
		$scope.sortChanged = function (grid, sortColumns) {
			var sortArray = new Array();
			for (var i=0; i<sortColumns.length; i++) {
				sortArray.push({
					"name" : sortColumns[i].field,
					"priority" : sortColumns[i].sort.priority,
					"direction" : sortColumns[i].sort.direction
				});
			}
			$scope.sortArray = sortArray;
			$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		};
		
		$scope.showTender = function(entity) {
			$state.go('administrator.tender_edit', { 'id' : entity.id, 'mode' : 'show' });
		};

		$scope.newTender = function(entity) {
			$state.go('administrator.tender_edit', { 'id' : 0, 'mode' : 'edit' });
		};
		
		$scope.editTender = function(entity) {
			$state.go('administrator.tender_edit', { 'id' : entity.id, 'mode' : 'edit' });
		};
		
		$scope.activateTender = function(entity) {
			Administrator_TenderService.activateTender(entity.id)
				.success(function(data, status) {
					toastr.success('Natječaj je uspješno aktiviran');
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				})
				.error(function(data, status) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom aktiviranja natječaja');
					}
				});
		};
		
		$scope.deactivateTender = function(entity) {
			Administrator_TenderService.deactivateTender(entity.id)
				.success(function(data, status) {
					toastr.success('Natječaj je uspješno deaktiviran');
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				})
				.error(function(data, status) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom deaktiviranja natječaja');
					}
				});
		};
		
		$scope.deleteTender = function(entity) {
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_DEFAULT,
	            title: 'Obriši natječaj',
	            message: 'Da li doista želite obrisati natječaj \'' + entity.name + '\'?',
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
		                	Administrator_TenderService.deleteTender(entity.id)
			    				.success(function(data, status) {
			    					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			    					toastr.success('Natječaj je uspješno obrisan');
			    				})
			    				.error(function(data, status) {
			    					if (status == 403) {
			    						$state.go('login');
			    					} else {
			    						toastr.error('Došlo je do pogreške prilikom brisanja natječaja');
			    					}
			    				});
		        			dialog.close();
		                }	                
	            	}
	            ]
	        });
		}
		
		// watch 'totalTenders' variable, so if it changes we can refresh grid
		$scope.$watch('totalTenders', function(newValue, oldValue) {
			if (newValue != oldValue) {
				$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			}
		});
		
		// initial load
		$scope.getPage(1, 10);
})

// ==============================================================================================================
// 	EDIT
// ==============================================================================================================
.controller('Administrator_TenderEditCtrl', function($rootScope, $scope, $state, $sce, $stateParams, Administrator_ConfigurationService, Administrator_InvestmentService, Administrator_TenderService) {
	
	$scope.mode = $stateParams.mode;
	$scope.id = $stateParams.id
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	$scope.cancel = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	$scope.edit = function() {
		$state.go('administrator.tender_edit', { 'id' : $scope.tender.id, 'mode' : 'edit' });
	};
	
	$scope.save = function() {
		Administrator_TenderService.saveTender($scope.tender)
			.success(function(data, status) {
				$scope.tender = data;
				toastr.success('Natječaj je uspješno spremljen');
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja natječaja');
				}
			});
	}
	
	Administrator_TenderService.getTender($stateParams.id)
		.success(function(data, status) {
			$scope.tender = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja natječaja');
			}
		});
	
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
	
	/** text editor options */
	$scope.summernoteOptions = {
		height: 300,
		focus: false,
		airMode: false,
		toolbar: [['style', ['bold', 'italic', 'underline']], ['alignment', ['ul', 'ol', 'paragraph']], ['insert', ['link','hr']]]
	};
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
});
