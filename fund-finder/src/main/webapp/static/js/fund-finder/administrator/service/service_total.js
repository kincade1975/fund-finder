angular.module('fundFinder')

	.service('Administrator_TotalService', function($rootScope, $state, $http) {
		
		$rootScope.autoUpdateStarted = false;
		
		this.startAutoUpdate = function() {
			if ($rootScope.authenticated && !$rootScope.autoUpdateStarted) {
				var socket = new SockJS('/stomp');
				var stompClient = Stomp.over(socket);
				stompClient.connect({ }, function(frame) {
					$rootScope.subscription = stompClient.subscribe("/topic/total", function(message) {
				    	var json = JSON.parse(message.body);
				    	setTimeout(function () {
				    		$rootScope.$apply(function () {
				    			if ($rootScope.totalUsers != json.totalUsers) {
				    				$rootScope.totalUsers = json.totalUsers;
				    			}
				    			if ($rootScope.totalTenders != json.totalTenders) {
				    				$rootScope.totalTenders = json.totalTenders;
				    			}
				    			if ($rootScope.totalInvestments != json.totalInvestments) {
				    				$rootScope.totalInvestments = json.totalInvestments;
				    			}
				    			if ($rootScope.totalArticles != json.totalArticles) {
				    				$rootScope.totalArticles = json.totalArticles;
				    			}
				            });
				        }, 500);
				    });
				});
	
				$rootScope.autoUpdateStarted = true;
			}
		}
		
		this.updateTotal = function() {
			if ($rootScope.authenticated) {
				$http.get('/api/v1/total')
					.success(function(data, status, headers, config) {
						$rootScope.totalUsers = data.totalUsers;
						$rootScope.totalTenders = data.totalTenders;
						$rootScope.totalInvestments = data.totalInvestments;
						$rootScope.totalArticles = data.totalArticles;
					})
					.error(function(data, status, headers, config) {
						if (status == 403) {
							$state.go('login');
						} else {
							toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
						}
					});
			}
		}
		
	});