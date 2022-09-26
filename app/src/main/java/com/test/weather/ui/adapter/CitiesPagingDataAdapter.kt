package com.test.weather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.weather.data.entity.City
import com.test.weather.databinding.ItemCityBinding

class CitiesPagingDataAdapter(private val callBack: (City) -> Unit) : PagingDataAdapter<City, CitiesPagingDataAdapter.CitiesViewHolder>(CitiesEntityDiff()) {

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        getItem(position)?.let { userPostEntity -> holder.bind(userPostEntity) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class CitiesViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.position = city.primaryKey
            binding.name = city.name
            itemView.setOnClickListener{
                callBack(city)
            }
        }
    }
  
  class CitiesEntityDiff : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean = oldItem.name == newItem.name
  }
}