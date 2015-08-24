angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('Administrator_ArticleOverviewCtrl', function($rootScope, $scope, $state, Administrator_TotalService, Administrator_ArticleService) {
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
					width: 82,
					cellTemplate:'<div style="padding-top: 1px"><button ng-click="grid.appScope.editArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-edit"></i></button><button ng-click="grid.appScope.deleteArticle(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-times"></i></button></div>'
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
			    					Administrator_TotalService.updateTotal();
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
.controller('Administrator_ArticleEditCtrl', function($rootScope, $scope, $state, $sce, $stateParams, Administrator_TotalService, Administrator_ArticleService) {
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
				Administrator_TotalService.updateTotal();
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
	            ['insert', ['link','picture','hr']]
	        ]
	  };
	
});
