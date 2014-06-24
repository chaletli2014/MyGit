<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="en-US">
<head>  
    <title> 
                    标题 
    </title>
    <meta name="viewport" content="width=device-width, initial-scale="1">
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.0a3/jquery.mobile-1.0a3.min.css"/>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.5.min.js"></script>
    <script type="text/javascript" src="http://code.jquery.com/mobile/1.0a3/jquery.mobile-1.0a3.min.js"></script>
    
    <link rel="stylesheet" href="./css/jquery.mobile.simpledialog.css" type="text/css"></link>
    <link rel="stylesheet" href="./css/datepicker.css" type="text/css"/>
    <script type="text/javascript" src="./js/dFormat.js"></script>
    <script type="text/javascript" src="./js/datepicker.js"></script>
    <script type="text/javascript" src="./js/jquery.mobile.simpledialog.js"></script>
    
</head>
<body>
    <div style="position:absolute; left:-9999px;"><a href="#" id="setfoc"></a></div>
    <div data-role="page" id="home">
        <div data-role="header" data-position=”fixed”>
            <h1>Header</h1>
        </div>
        <div data-role="content">
            <div data-role="fieldcontain">
                <label for="hospital" >选择医院：</label>
                <select name="hospital" id="hospital">
                    <option value="1">医院一</option>
                    <option value="1">医院二</option>
                    <option value="1">医院三</option>
                </select>
            </div>
        </div>
        <div data-role="footer">
            <h4>Footer</h4>
        </div>
    </div>
</body>  
</html>  