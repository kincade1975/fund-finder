angular.module('fundFinder')

//==============================================================================================================
//	OVERVIEW
//==============================================================================================================
.controller('Administrator_UserOverviewCtrl', function($rootScope, $scope, $state, Administrator_TotalService, Administrator_UserService) {
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
				name: 'Ime',
				field: 'firstName',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false
			},
			{
				name: 'Prezime',
				field: 'lastName',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false
			},
			{
				name: 'Akcija',
				type: 'string',
				cellTooltip: false, 
				enableSorting: false,
				enableFiltering: false,
				enableHiding: false,
				width: 150,
				cellTemplate:'<div style="margin:3px;"><button ng-click="grid.appScope.showUser(row.entity)" class="btn-xs btn-success"><i class="fa fa-eye"></i> Detalji</button><button ng-click="grid.appScope.deleteUser(row.entity)" class="btn-xs btn-danger m-l-xs"><i class="fa fa-times"></i> Obriši</button></div>'
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
		
		Administrator_UserService.getPage(uiGridResource).
			success(function(data, status, headers, config) {
				$scope.gridOptions.data = data.data;
				$scope.gridOptions.totalItems = data.total;

				var newHeight = data.data.length * 30 + (($scope.gridOptions.totalItems == 0) ? 90 : 92);
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
	
	$scope.deleteUser = function(entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: 'Obriši korisnika',
            message: 'Da li doista želite obrisati korisnika \'' + entity.firstName + ' ' + entity.lastName + '\'?',
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
	                	Administrator_UserService.deleteUser(entity.id)
		    				.success(function(data, status) {
		    					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					toastr.success('Korisnik je uspješno obrisan');
		    					Administrator_TotalService.updateTotal();
		    				})
		    				.error(function(data, status) {
		    					if (status == 403) {
		    						$state.go('login');
		    					} else {
		    						toastr.error('Došlo je do pogreške prilikom brisanja korisnika');
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