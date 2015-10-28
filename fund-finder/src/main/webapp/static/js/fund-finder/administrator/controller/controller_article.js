angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('Administrator_ArticleOverviewCtrl', function($rootScope, $scope, $state, Administrator_ArticleService) {
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
					field: 'title',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					cellTemplate:'<div class="ui-grid-cell-contents"><a class="custom-a" ng-click="grid.appScope.showArticle(row.entity)">{{row.entity.title}}</a></div>'
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
					width: 170,
					cellTemplate:
						'<div style="padding-top: 1px">' +
						'<button ng-click="grid.appScope.showStatistics(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-bar-chart"></i></button>' +
						'<button ng-show="row.entity.active == false" ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.activateArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
						'<button ng-show="row.entity.active == true" ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.deactivateArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-on"></i></button>' +
						'<button ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.editArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-edit"></i></button>' +
						'<button ng-disabled="grid.appScope.role == \'ROLE_ADMINISTRATOR_RO\'" ng-click="grid.appScope.deleteArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-times"></i></button>' + 
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
			
			Administrator_ArticleService.getPage(uiGridResource).
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
		
		$scope.showArticle = function(entity) {
			$state.go('administrator.article_edit', { 'id' : entity.id, 'mode' : 'show' });
		};

		$scope.newArticle = function(entity) {
			$state.go('administrator.article_edit', { 'id' : 0, 'mode' : 'edit' });
		};
		
		$scope.editArticle = function(entity) {
			$state.go('administrator.article_edit', { 'id' : entity.id, 'mode' : 'edit' });
		};
		
		$scope.showStatistics = function(entity) {
			$state.go('administrator.article_statistics', { 'id' : entity.id });
		};
		
		$scope.deleteArticle = function(entity) {
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_DEFAULT,
	            title: 'Obriši članak',
	            message: 'Da li doista želite obrisati članak \'' + entity.title + '\'?',
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
		                	Administrator_ArticleService.deleteArticle(entity.id)
			    				.success(function(data, status) {
			    					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			    					toastr.success('Članak je uspješno obrisan');
			    				})
			    				.error(function(data, status) {
			    					if (status == 403) {
			    						$state.go('login');
			    					} else {
			    						toastr.error('Došlo je do pogreške prilikom brisanja članka');
			    					}
			    				});
		        			dialog.close();
		                }	                
	            	}
	            ]
	        });
		}
		
		$scope.activateArticle = function(entity) {
			Administrator_ArticleService.activateArticle(entity.id)
				.success(function(data, status) {
					toastr.success('Članak je uspješno aktiviran');
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				})
				.error(function(data, status) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom aktiviranja članka');
					}
				});
		};
		
		$scope.deactivateArticle = function(entity) {
			Administrator_ArticleService.deactivateArticle(entity.id)
				.success(function(data, status) {
					toastr.success('Članak je uspješno deaktiviran');
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				})
				.error(function(data, status) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom deaktiviranja članka');
					}
				});
		};
		
		// watch 'totalArticles' variable, so if it changes we can refresh grid
		$scope.$watch('totalArticles', function(newValue, oldValue) {
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
.controller('Administrator_ArticleEditCtrl', function($rootScope, $scope, $state, $sce, $stateParams, Upload, Administrator_ArticleService) {
	$scope.mode = $stateParams.mode;
	$scope.id = $stateParams.id;
	
	$scope.back = function() {
		$state.go('administrator.article_overview');
	};
	
	$scope.cancel = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	$scope.edit = function() {
		$state.go('administrator.article_edit', { 'id' : $scope.article.id, 'mode' : 'edit' });
	};
	
	$scope.save = function() {
		Administrator_ArticleService.saveArticle($scope.article)
			.success(function(data, status) {
				$scope.article = data;
				toastr.success('Članak je uspješno spremljen');
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja članka');
				}
			});
	}
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
	Administrator_ArticleService.getArticle($stateParams.id)
		.success(function(data, status) {
			$scope.article = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja članka');
			}
		});
	
	$scope.options = {
	    height: 500,
	    focus: false,
	    airMode: false,
	    toolbar: [
	            ['style', ['bold', 'italic', 'underline']],
	            ['alignment', ['ul', 'ol', 'paragraph']],
	            ['insert', ['link','hr']]
	        ]
	  };
	
	$scope.upload = function(files) {
		if (files && files.length == 1) {
			Upload.upload({
		        url: '/api/v1/file/upload',
		        method: 'POST',
		        file: files[0]
		    }).success(function(data, status, headers, config) {
		    	$scope.article.base64 = data.base64;
		    	$scope.article.image = data.name;
		    }).error(function(data, status, headers, config) {
		    	toastr.error('Došlo je do pogreške prilikom uploada slike');
		    });
		}
	};
	
	$scope.removeImage = function() {
		$scope.article.base64 = null;
		$scope.article.image = null;
	}
	
})

// ==============================================================================================================
//	STATISTICS
// ==============================================================================================================
.controller('Administrator_ArticleStatisticsCtrl', function($rootScope, $scope, $state, $stateParams, Administrator_ImpressionService) {
	$scope.id = $stateParams.id;
	
	var colours = new Array();
	colours.push("#1ab394");
	colours.push("#f8ac59");
	$scope.colours = colours;
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	Administrator_ImpressionService.getStatisticsPeriods()
		.success(function(data, status) {
			$scope.statisticsPeriods = data;
			$scope.statisticsPeriod = "LAST_7_DAYS";
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	$scope.getImpressionStatistics = function(statisticsPeriod) {
		Administrator_ImpressionService.getImpressionStatistics("ARTICLE", $stateParams.id, (statisticsPeriod) ? statisticsPeriod : "LAST_7_DAYS")
			.success(function(data, status) {
				$scope.impressionStatistics = data;
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
				}
			});
	};
	
	$scope.statisticsPeriodChanged = function() {
		$scope.getImpressionStatistics($scope.statisticsPeriod);
	};
	
	$scope.getImpressionStatistics();
	
});

