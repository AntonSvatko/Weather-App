package com.test.weather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.weather.data.entity.City
import com.test.weather.databinding.ItemCityBinding

class CitiesAdapter(private val callBack: (City) -> Unit) :
    ListAdapter<City, CitiesAdapter.WeatherViewHolder>(WeatherDiffUtilVoiceRec()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindVoiceRec(getItem(position))
    }

    inner class WeatherViewHolder(
        private val binding: ItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindVoiceRec(city: City) {
            binding.name = city.name
            binding.position = city.primaryKey

            itemView.setOnClickListener {
                callBack(city)
            }
            binding.executePendingBindings()
        }
    }


    class WeatherDiffUtilVoiceRec : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(
            oldItemVoiceRec: City,
            newItemVoiceRec: City
        ): Boolean {
            return oldItemVoiceRec.id == newItemVoiceRec.id
        }

        override fun areContentsTheSame(
            oldIteVoiceRec: City,
            newItemVoiceRec: City
        ): Boolean {
            return oldIteVoiceRec.name == newItemVoiceRec.name
        }
    }
}