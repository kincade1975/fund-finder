angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('ArticleOverviewCtrl', function($rootScope, $scope, $state, TotalService, ArticleService) {
	$scope.gridOptions = {
			enableScrollbars: false,
			paginationPageSizes: [10, 20, 30, 50, 100],
			paginationPageSize: 10,
			useExternalPagination: true,
			enableFiltering: true,
			useExternalFiltering: true,
			totalItems: 10,
			rowHeight: 30,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 0,
			enableColumnMenus: false,
			enableGridMenu: false,
			columnDefs: [
				{
					name: 'Naziv članka',
					field: 'title',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false
				},
				{
					name: 'Kreiran',
					field: 'timeCreated',
					type: 'date',
					cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',
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
					width: 210,
					cellTemplate:'<div style="margin:3px;"><button ng-click="grid.appScope.showArticle(row.entity)" class="btn-xs btn-success"><i class="fa fa-eye"></i> Detalji</button><button ng-click="grid.appScope.editArticle(row.entity)" class="btn-xs btn-success m-l-xs"><i class="fa fa-edit"></i> Uredi</button><button ng-click="grid.appScope.deleteArticle(row.entity)" class="btn-xs btn-danger m-l-xs"><i class="fa fa-times"></i> Obriši</button></div>'
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
			
			ArticleService.getPage(uiGridResource).
				success(function(data, status, headers, config) {
					$scope.gridOptions.data = data.data;
					$scope.gridOptions.totalItems = data.total;

					var newHeight = data.data.length * 30 + (($scope.gridOptions.totalItems == 0) ? 90 : 90);
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
			$state.go('fund_finder.article_edit', { 'id' : entity.id, 'mode' : 'show' });
		};

		$scope.newArticle = function(entity) {
			$state.go('fund_finder.article_edit', { 'id' : 0, 'mode' : 'edit' });
		};
		
		$scope.editArticle = function(entity) {
			$state.go('fund_finder.article_edit', { 'id' : entity.id, 'mode' : 'edit' });
		};
		
		$scope.deleteArticle = function(entity) {
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_DEFAULT,
	            title: 'Obriši članak',
	            message: 'Da li doista želite obrisati članak \'' + entity.title + '\'?',
	            buttons: [
					{
						label: 'No',
						icon: 'fa fa-times',
					    cssClass: 'btn-white',
					    action: function(dialog) {
					        dialog.close();
					    }
					},
	            	{
	            		label: 'Yes',
		            	icon: 'fa fa-check',
		                cssClass: 'btn-primary',
		                action: function(dialog) {
		                	ArticleService.deleteArticle(entity.id)
			    				.success(function(data, status) {
			    					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			    					toastr.success('Članak je uspješno obrisan');
			    					TotalService.updateTotal();
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
		
		// initial load
		$scope.getPage(1, 10);
})

// ==============================================================================================================
// 	EDIT
// ==============================================================================================================
.controller('ArticleEditCtrl', function($rootScope, $scope, $state, $stateParams, TotalService, ArticleService) {
	$scope.mode = $stateParams.mode;
	
	$scope.back = function() {
		$state.go('fund_finder.article_overview');
	};
	
	$scope.cancel = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	$scope.edit = function() {
		$state.go('fund_finder.article_edit', { 'id' : $scope.article.id, 'mode' : 'edit' });
	};
	
	$scope.save = function() {
		ArticleService.saveArticle($scope.article)
			.success(function(data, status) {
				$scope.article = data;
				toastr.success('Članak je uspješno spremljen');
				TotalService.updateTotal();
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja članka');
				}
			});
	}
	
	ArticleService.getArticle($stateParams.id)
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
	
});
