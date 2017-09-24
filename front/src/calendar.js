function Calendar(divId){
  this.divId = divId
}

Calendar.prototype.init = function(){
  $(this.divId).fullCalendar({
    })
}
