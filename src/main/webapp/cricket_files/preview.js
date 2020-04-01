/*global angular, window, document, $, QRCode, _ */

angular.module('AppPreview', ['ui.bootstrap', 'wm.studio.PreviewWindow'])
    .config(function ($locationProvider, $qProvider) {
        'use strict';

        $locationProvider.hashPrefix('');
        $qProvider.errorOnUnhandledRejections(false);
    })
    .controller('AppPreviewController', function ($scope, $http, $location, $sce, $timeout, PREVIEW_CONSTANTS, $uibModal, $rootScope) {
        'use strict';

        function constructAppUrl(allParams) {
            var url = allParams.app_url,
                prefix = '&',
                queryParamIgnoreList = ['app_type', 'app_url', 'app_name', 'platform_type', 'device', 'app_displayname', 'device_types', 'selectedlocale']; // params which are to be removed

            if (!_.includes(url, '?')) {
                prefix = '?';
            }

            url = _.keys(allParams)
                .filter(function (param) {
                    return !_.includes(queryParamIgnoreList, param);
                }).reduce(function (_url, param) {
                    _url = _url + prefix + param + '=' + allParams[param];
                    prefix = '&';
                    return _url;
                }, url);

            return $sce.trustAs($sce.RESOURCE_URL, url);
        }

        function getAppBaseUrl(_url) {
            var parser  = document.createElement('a'); // create a dummy link to construct the base path
            parser.href = _url;

            return parser.protocol + '//' + parser.host + parser.pathname;
        }

        var qrcode,
            appCtrl = this,
            queryParams = $location.search(),
            MODE = {
                'DESIGN': 'DESIGN',
                'PREVIEW': 'PREVIEW',
                'DESIGNANDPREVIEW': 'DESIGNANDPREVIEW'
            },
            APP_TYPE = {
                'APPLICATION': 'APPLICATION',
                'PREFAB': 'PREFAB',
                'TEMPLATEBUNDLE': 'TEMPLATEBUNDLE'
            },
            MSGS        = {
                'HIDE_TEMPLATES_SHOW_CASE': 'hide-templates-show-case',
                'SHOW_TEMPLATES_SHOW_CASE': 'show-templates-show-case',
                'UPDATE_LOCATION': 'update-location-path',
                'SELECT_TEMPLATE': 'select-template',
                'SWITCH_DEVICE': 'switch-device',
                'TEMPLATEBUNDLE_CONFIG': 'template-bundle-config',
                'ON_LOAD': 'on-load',
                'PREFAB_CONFIG': 'prefab:config',
                'PREFAB_EVENT_LOG': 'prefab:event-log',
                'PREFAB_OUTBOUND_PROPS': 'prefab:outbound-props-info'
            },
            ROOT_DIR = '//dh2dw20653ig1.cloudfront.net/studio/10.3.0.2/editor/scripts/modules/layouts/deviceTypes/',
            deviceJsonUrl = ROOT_DIR + queryParams.app_type.toLowerCase() + '/' + queryParams.platform_type.toLowerCase() + '/devices.json',
            targetFrame = document.getElementById('app-view'),
            targetFrameWindow = targetFrame.contentWindow,
            qrcodeTarget = document.getElementById('qrcode'),
            appName = queryParams.app_displayname || queryParams.app_name,
            targetDevice = queryParams.device,
            jsonEditorOptions = {navigationBar: false, statusBar: false};


        document.title = appName;
        //add a window name which can be useful for runmode use-cases
        window.name = PREVIEW_CONSTANTS.WINDOW_NAME;

        appCtrl.appType = queryParams.app_type;
        appCtrl.appUrl = constructAppUrl(queryParams);
        appCtrl.appBaseUrl = getAppBaseUrl(appCtrl.appUrl);
        appCtrl.title = document.title;

        appCtrl.isTemplateBundleType = appCtrl.appType === APP_TYPE.TEMPLATEBUNDLE;
        appCtrl.isPrefabType = appCtrl.appType === APP_TYPE.PREFAB;
        appCtrl.frameLocation = appCtrl.appUrl;
        appCtrl.isMobileApplicationType = queryParams.platform_type === 'MOBILE';

        qrcode = new QRCode(qrcodeTarget, {
            width: 200,
            height: 200
        });

        appCtrl.hideQRCode = true;

        appCtrl.prefabEventLogs = [];
        appCtrl.haveInBoundProps = false;
        appCtrl.haveOutBoundProps =  false;
        appCtrl.haveAnyMethods = false;
        appCtrl.localeFilePath = '//dh2dw20653ig1.cloudfront.net/studio/10.3.0.2/editor/scripts/modules/i18n/messages/'+ queryParams.selectedlocale + '.json';

        $http.get(appCtrl.localeFilePath).then(function (response){
            appCtrl.locale = response.data;
        });

        qrcode.makeCode(queryParams.app_url);

        function postMessage(content) {
            targetFrameWindow.postMessage(content, '*');
        }

        function registerIFrameMsgListeners() {
            window.onmessage = function (msg) {
                var msgData = null, key;

                msgData = msg.data;

                if (!angular.isObject(msgData)) {
                    return;
                }

                key = msgData.key;

                var action = msgData.action, context = msgData.context, payload = msgData.payload;

                if (context === 'PREFAB') {
                    if (action === 'init') {
                        postMessage({
                            context: 'PREFAB',
                            action: 'init'
                        });
                    } else if (action === 'config') {
                        initPrefab(payload);
                    } else if (action === 'event-log') {
                        appCtrl.prefabEventLogs.push(payload);
                    } else if (action === 'outbound-properties') {
                        appCtrl.prefabOutBoundPropsInfo = payload;
                    } else if (action === 'method-output') {
                        appCtrl.methodOutputInfo = payload;

                        $timeout(function() {
                            var methodDetails = appCtrl.prefabConfig.methods[payload.methodName];

                            if (methodDetails.outEditor) {
                                if (angular.isDefined(payload.output)) {
                                    methodDetails.outEditor.set(payload.output);
                                } else {
                                    payload.output = 'undefined';
                                    methodDetails.outEditor.setText('undefined');
                                }
                            }

                            methodDetails.output = payload.output;
                        });
                    }
                } else {
                    switch (key) {
                        case MSGS.TEMPLATEBUNDLE_CONFIG:
                            appCtrl.templates = msgData.config.templates;
                            break;
                        case MSGS.UPDATE_LOCATION:
                            appCtrl.frameLocation = msgData.location;
                            qrcode.makeCode(appCtrl.frameLocation);
                            break;
                        case MSGS.ON_LOAD:
                            postMessage({'key': MSGS.HIDE_TEMPLATES_SHOW_CASE});
                            postMessage({'key': MSGS.SWITCH_DEVICE, 'device': appCtrl.selectedDevice});
                            break;
                    }
                }

                $scope.$apply();
            };
        }

        registerIFrameMsgListeners();

        if (appCtrl.isTemplateBundleType) {
            appCtrl.activeTemplateIndex = 0;
            appCtrl.selectTemplate = function (index) {
                appCtrl.activeTemplateIndex = index;
                postMessage({'key': MSGS.SELECT_TEMPLATE, 'templateIndex': index});
            };
        }

        appCtrl.selectedDevice = {
            width: '100%',
            height: '99%'
        };

        $http.get(deviceJsonUrl)
            .then(function (response) {
                var data = response.data;
                if (queryParams.platform_type === 'MOBILE') {
                    if (queryParams.device_types === 'TABLET') {
                        delete data.Smartphone;
                    } else {
                        delete data.Tab;
                    }
                }
                appCtrl.allDevices = {};

                _.keys(data)
                    .forEach(function (category) {
                        var categoryDetails  = data[category],
                            selectedDevices  = _.filter(categoryDetails.devices, {'id': targetDevice});
                        if (selectedDevices.length) {
                            appCtrl.selectedDevice         = selectedDevices[0];
                            appCtrl.selectedDeviceCategory = category;
                        }
                        if (_.includes(categoryDetails.modes, MODE.PREVIEW)) {
                            appCtrl.allDevices[category] = categoryDetails;
                        }
                    });

                appCtrl.deviceCategories = _.keys(data);
            });

        appCtrl.onDeviceTypeChange = function (device, category) {
            appCtrl.selectedDevice         = device;
            appCtrl.selectedDeviceCategory = category;
            try {
                // if the application is of templateBundle reload the active page.
                // otherwise load the landing page of the application.
                if (!appCtrl.isTemplateBundleType) {
                    targetFrameWindow.location = appCtrl.appUrl;
                }
                targetFrameWindow.location.reload();
            } catch (e) {
                //If error (cross-origin) happens just trigger device change
                postMessage({'key': MSGS.SWITCH_DEVICE, 'device': device});
            }
        };

        // This function sets the localStorage item hideWaveLensDialog to true.
        appCtrl.closeWavelensDialog = function () {
            if (window.localStorage) {
                window.localStorage.setItem('hideWaveLensDialog', true);
            }
        };

        // This function returns the localStorage item, whether to show the wavelens dialog or not.
        appCtrl.getShowProp = function () {
            return !window.localStorage.getItem('hideWaveLensDialog');
        };

        targetFrame.onload = function () {
            var loc = targetFrameWindow.location;
            qrcode.makeCode(loc.href);

            if (loc.hostname !== 'localhost') {
                appCtrl.hideQRCode = false;
                $scope.$apply();
            }
        };

        appCtrl.fetchOutBoundProps = function () {
            postMessage({
                context: 'PREFAB',
                action: 'get-outbound-properties'
            });
        };

        appCtrl.showPrefabPropertyBindDialog = function (propName, propDetails, outBoundValue) {
            $uibModal.open({
                templateUrl: 'prefabPropertyBindingDialog.html',
                controller: 'PrefabPropertyBindingDialogController',
                controllerAs: '$ctrl',
                windowClass: 'prefab-props-dialog wm-studio-dialog',
                resolve: {
                    outBoundValue: function () {
                        return outBoundValue
                    },
                    property: function () {
                        return {
                            name: propName,
                            details: propDetails
                        };
                    },
                    postMessage: function () {
                        return postMessage
                    },
                    cacheValue: function () {
                        return cacheValue
                    },
                    parentScope: appCtrl
                }
            });
        };

        appCtrl.onPrefabPropertyChange = function (propName) {
            var info = appCtrl.prefabConfig.properties[propName];
            cacheValue(propName, info.value, info.boundValue);
            postMessage({
                context: 'PREFAB',
                action: 'set-property',
                payload: {
                    name: propName,
                    value: info.value
                }
            });
        };

        function getStorageKey() {
            return '_wm_prefab_' + appCtrl.prefabConfig.displayName + '_props_';
        }

        function cacheValue(propName, value, boundValue) {
            var storageKey = getStorageKey();

            appCtrl.cachedProps[propName] = {
                value: value,
                boundValue: boundValue
            };

            sessionStorage.setItem(storageKey, JSON.stringify(appCtrl.cachedProps));
        }

        function readCachedProperties() {

            // read the saved state from session storage
            var storageKey = getStorageKey();
            var cached = {};
            var props = appCtrl.prefabConfig.properties;

            try {
                cached = JSON.parse(sessionStorage.getItem(storageKey)) || {};
            } catch (e) {
                // invalid json in session storage
            }

            appCtrl.cachedProps = cached;

            _.forEach(cached, function (propInfo, propName) {
                if (propInfo) {
                    var instanceValue = propInfo.boundValue || propInfo.value;

                    if (!_.isUndefined(propInfo.value)) {
                        if (props[propName]) {
                            props[propName].value = propInfo.value;
                        } else {
                            cached[propName] = undefined;
                        }
                    }

                    if (!_.isUndefined(propInfo.boundValue)) {
                        if (props[propName]) {
                            props[propName].boundValue = propInfo.boundValue;
                        } else {
                            cached[propName] = undefined;
                        }
                    }

                    postMessage({
                        context: 'PREFAB',
                        action: 'set-property',
                        payload: {
                            name: propName,
                            value: instanceValue
                        }
                    });
                }
            });
        }

        function initPrefab (config) {
            appCtrl.prefabConfig = config;

            readCachedProperties();

            appCtrl.prefabEventLogs = [];


            _.forEach(appCtrl.prefabConfig.properties, function (prop) {
                // Check for whether any out-bound properties exists
                if(!appCtrl.haveOutBoundProps && ['out-bound', 'in-out-bound'].indexOf(prop.bindable) !== -1) {
                    appCtrl.haveOutBoundProps = true;
                }

                // Check for whether any in-bound properties exists
                if(!appCtrl.haveInBoundProps && ['in-bound', 'in-out-bound'].indexOf(prop.bindable) !== -1) {
                    appCtrl.haveInBoundProps = true;
                }

                if (prop.value && prop.type === 'date') {
                    prop.value = new Date(prop.value);
                }

                if (prop.options && (!_.isArray(prop.options) || _.startsWith(prop.options, 'bind:'))) {
                    prop.widget = 'text';
                }
            });

            if(!_.isEmpty(appCtrl.prefabConfig.methods)) {
                appCtrl.haveAnyMethods = true;
            }
        }

        appCtrl.clearLogs = function () {
            appCtrl.prefabEventLogs = [];
        };

        appCtrl.showEventLogData = function (log) {
            appCtrl.selectedLog = log;
            $uibModal.open({
                templateUrl: 'eventLogData.html',
                windowClass: 'prefab-props-dialog wm-studio-dialog'
            }).opened.then(function () {
                $timeout(function () {
                    var container = document.getElementById('event_log_data');
                    var editor = new JSONEditor(container, _.assign({mode: 'code'}, jsonEditorOptions));
                    editor.set(log.data || '');
                    editor.aceEditor.setReadOnly(true);
                }, 200)
            });
        };

        appCtrl.showOutBoundPropsDialog = function (propName, propValue) {
            var modalInstance = $uibModal.open({
                templateUrl: 'outBoundPropsInfo.html',
                windowClass: 'prefab-props-dialog wm-studio-dialog'
            });

            $rootScope.selectedOutBoundProperty = {
                name: propName,
                value: propValue
            };

            $rootScope.locale = this.locale;

            modalInstance.opened.then(function () {
                $timeout(function () {
                    var container = document.getElementById('out_bound_props');
                    var editor = new JSONEditor(container, {mode: 'code'});
                    editor.aceEditor.setReadOnly(true);
                    editor.set(propValue  || '');
                });
            });
        };

        appCtrl.invokeMethod = function (methodName, methodDetails) {

            var editor = methodDetails.inEditor;
            var script = editor.getText();
            if (script) {
                script = script.trim();

                methodDetails.script = script;

                if (script.length) {
                    postMessage({
                        context: 'PREFAB',
                        action: 'invoke-script',
                        payload: {
                            methodName: methodName,
                            script: script
                        }
                    });
                }
            }
        };

        appCtrl.initMethodEditors = function (group, context, methodName, methodDetails) {

            var deregister = context.$watch('group.open', function (nv) {
                if (nv) {
                    deregister();
                    $timeout(function () {
                        var container = document.getElementById('method_input_' + methodName);
                        var editor = new JSONEditor(container, _.assign({mode: 'text'}, jsonEditorOptions));
                        methodDetails.inEditor = editor;
                        editor.setText('Prefab.' + methodName + '()');

                        container = document.getElementById('method_output_' + methodName);
                        editor = new JSONEditor(container, _.assign({mode: 'code'}, jsonEditorOptions));
                        editor.aceEditor.setReadOnly(true);
                        methodDetails.outEditor = editor;

                    }, 500);
                }
            });
        };

        appCtrl.initEventLogEditors = function (group, context, log, $index) {
            var deregister = context.$watch('group.open', function (nv) {
                if (nv) {
                    deregister();
                    $timeout(function () {
                        var container = document.getElementById('event_log_' + $index);
                        var editor = new JSONEditor(container, _.assign({mode: 'code'}, jsonEditorOptions));
                        editor.aceEditor.setReadOnly(true);
                        editor.set(log.data);

                    }, 500);
                }
            });
        }
    })
    .controller('PrefabPropertyBindingDialogController',
        function($scope, property, $uibModal, postMessage, cacheValue, $timeout, parentScope) {

            $ctrl = this;

            $ctrl.property = property;
            $ctrl.locale = parentScope.locale;
            var editor;

            $timeout(function () {
                var container = document.getElementById('property_binding');
                editor = new JSONEditor(container, {mode: 'text'});
                editor.setText(property.details.boundValue || '');
            }, 200);

            function _cacheValue() {
                cacheValue(property.name, property.details.value, property.details.boundValue);
            }

            function updateValue(value) {

                try {
                    value = JSON.parse(value);
                } catch (e) {

                }

                postMessage({
                    context: 'PREFAB',
                    action: 'set-property',
                    payload: {
                        name: property.name,
                        value: value
                    }
                });
            }

            $ctrl.reset = function () {
                property.details.boundValue = '';
                updateValue(property.details.value);
                _cacheValue();
                $scope.$close();
            };

            $ctrl.save = function () {
                property.details.boundValue = editor.getText();
                updateValue(property.details.boundValue);
                _cacheValue();
                $scope.$close();
            };
        }
    );
