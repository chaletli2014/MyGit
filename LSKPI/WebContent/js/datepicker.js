/**
 * jQuery Moblie Datepicker汉化版 plugin
 * 主要实现日期选择功能
 * @name Datepicker
 * @author 丁亚飞 - http://www.5niu.org（dgunzi http://www.5niu.org）
 * @version 0.1
 * @copyright (c) 2011 DingYafei (5niu.org)
 */
	datepicker = {
				cd:new Date(),
				current:null,
				windialog:null,
				/*Defaults*/
				initTime:function(){
				  datepicker.cd = new Date();
				  $('#dStr').html(dateFormat(datepicker.cd, "yyyy年mm月dd日 dddd"));
				  $('#mon').val(dateFormat(datepicker.cd, "mmm"));
				  $('#day').val(dateFormat(datepicker.cd, "dd"));
				  $('#year').val(dateFormat(datepicker.cd, "yyyy"));
				},
				initDialog:function(){
				  var top = ($(window).height() - $('#dialog').height()) / 2;
				  $(window).resize(function (t) {
					if (top < 0) { top = 0; }
					$('#dialog').css('left', ($(window).width() - $('#dialog').width()) / 2 + 'px').css('top', top + 'px');
				  });
				  $('#dialog').css('left', ($(window).width() - $('#dialog').width()) / 2 + 'px').css('top', top + 'px');
				  $('#dialog').jqm({ modal: true });
				},
				/*Functions*/
				updateF:function(){
				  $('#year').val(dateFormat(datepicker.cd, "yyyy"));
                  $('#mon').val(dateFormat(datepicker.cd, "mmm"));
                  $('#day').val(dateFormat(datepicker.cd, "dd"));
                  $('#dStr').html(dateFormat(datepicker.cd, "yyyy年mm月dd日 dddd"));
                  if(navigator.userAgent.indexOf("Firefox")==-1){
                  	$('#setfoc').focus();
                  }
                  return false;
				},
				init:function(){
					  /*Mods*/
					  $('#pyear').tap(function () {
						  datepicker.cd.setYear(datepicker.cd.getFullYear() + 1);
						  datepicker.updateF();
						  return false;
					  });
					  $('#pmon').tap(function () {
						  datepicker.cd.setMonth(datepicker.cd.getMonth() + 1);
						 datepicker.updateF();
						  return false;
					  });
					  $('#pday').tap(function () {
						  datepicker.cd.setDate(datepicker.cd.getDate() + 1);
						  datepicker.updateF();
						  return false;
					  });
					  $('#myear').tap(function () {
						  datepicker.cd.setYear(datepicker.cd.getFullYear() - 1);
						  datepicker.updateF();
						  // $('#year').focus();
						  return false;
					  });
					  $('#mmon').tap(function () {
						  datepicker.cd.setMonth(datepicker.cd.getMonth() - 1);
						  datepicker.updateF();
						  return false;
					  });
					  $('#mday').tap(function () {
						  datepicker.cd.setDate(datepicker.cd.getDate() - 1);
						  datepicker.updateF();
						  return false;
					  });
					  $('#close').tap(function () {
						  datepicker.windialog.close();
						  return false;
					  });
					  $('#set').tap(function (event) {
						  $('#'+datepicker.current).val(dateFormat(datepicker.cd, "yyyy-mm-dd"));
						  datepicker.windialog.close();
						  return false;
					  });
				},
				handler:function(event){
					datepicker.current = event.data.curdatetext;
					$('#setfoc').simpledialog();
					datepicker.init();
				    datepicker.initTime();
				    if(navigator.userAgent.indexOf("Firefox")==-1){
						$('#setfoc').focus();
					}
					return false;
				}
	};