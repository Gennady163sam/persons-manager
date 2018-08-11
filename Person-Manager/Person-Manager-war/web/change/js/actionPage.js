/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var mapsElement = new Array();
var count=1;

function mapElem(){
    var type;
    var find;
    
    this.getType = function(){
        return type;
    };
    
    this.getFind = function(){
        return find;
    };

}

function back(){
    $("form").slideUp(1000);
    location.href='../index.jsp';
}

function addBlock(){

    $("<br/>").insertAfter("#find");
    newFind = $("#find").clone();
    newFind.insertAfter("#find");
    newFind.attr("name", newFind.attr("name")+count);
    newType = $("#type").clone();
    newType.attr("name", newType.attr("name")+count);
    $("<br/><br/>").insertBefore(newType.insertAfter("#find"));
    createElement(newFind, newType);

}


function createElement(newFind, newType){
    var elem = new mapElem();
    elem.find = newFind;
    elem.type = newType;
    
    mapsElement[count] = elem;
    count++;
}

function funcBefore(){
    
}

function getJSONObject(){
    var jsonString = '{ "map" : [';
    if(mapsElement.length>0){
       mapsElement.forEach(function(item, i, mapsElement) {
            jsonString += '{"type" : "'+item.type.val()+'", "find" : "' + item.find.val() +'"},';
         });
        jsonString = jsonString.substring(0,jsonString.length - 1); 
    }
    jsonString += ' ]}';
    return jsonString;
}

function funcAfter(data){
    $("#result").empty();
    var result = JSON.parse(data);
    for(var i = 0; i<result.length;i++){
        $("#result").append("<h6 id='" + result[i].id 
                +"' class='parentId' onclick='setParent(this.id)' >"+result[i].name +"</h6>");
        
    }
    $("#result").show(1000);
}

function find(){
    $("#result").hide(1000);
    $.ajax({
        url:"../GetPersonServlet",
        type:"GET",
        data:({map: getJSONObject(),
               type: $("#type").val(),
               find: $("input[name='find']").val(),
               sort: 1
        }),
        dataType: "html",
        beforeSend: funcBefore,
        success: funcAfter
    });
}


$(document).ready(function(){
    $('#stend').hide();
    $("#stend").slideDown(1000);
    find();
    $("input[name='operation']").bind("click",find);
    $("#back").bind("click",back);
    $("#add").bind("click",addBlock);
    $("<br/>").insertAfter("#find");
    $("<br/>").insertAfter("#find");
    
    elem = new mapElem();
    elem.type = $("#type");
    elem.find = $("input[name='find']");
    mapsElement[0] = elem;
});
