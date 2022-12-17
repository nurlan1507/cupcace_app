package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
class OrderViewModel: ViewModel() {

    private var _quantity = MutableLiveData<Int>()
    private var _flavor = MutableLiveData<String>()
    private var _date = MutableLiveData<String>()
    private var _price = MutableLiveData<Double>()
    val dateOptions = getPickUpOptions()
    //getters
    val quantity : MutableLiveData<Int> = _quantity
    val flavor:MutableLiveData<String> = _flavor
    val date:MutableLiveData<String> = _date
    var price:LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }
    init {
        resetOrder()
    }
    //setters
    fun setQuantity(quantity:Int){
        _quantity.value = quantity
        updatePrice()
    }
    fun setFlavor(flavor:String){
        _flavor.value = flavor
    }
    fun setDate(date:String){
        _date.value = date
        updatePrice()
    }


    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun updatePrice(){
        Log.d("PRICE","I HAVE UPDATED")
        var calculatedPrice = (quantity.value?:0) * PRICE_PER_CUPCAKE
        if(dateOptions[0] == _date.value) calculatedPrice+= PRICE_FOR_SAME_DAY_PICKUP
        _price.value = calculatedPrice
    }



    private fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }



    private fun getPickUpOptions():List<String>{
        var options:MutableList<String> = mutableListOf()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        var calendar = Calendar.getInstance()
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return options
    }

}