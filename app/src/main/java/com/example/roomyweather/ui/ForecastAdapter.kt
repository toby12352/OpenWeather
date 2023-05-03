package com.example.roomyweather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roomyweather.R
import com.example.roomyweather.data.FiveDayForecast
import com.example.roomyweather.data.ForecastCity
import com.example.roomyweather.data.ForecastPeriod
import com.example.roomyweather.util.getTempUnitsDisplay
import com.example.roomyweather.util.openWeatherEpochToDate

/**
 * This is the constructor for a RecyclerView adapter for five-day forecast data.
 *
 * @param onClick This should be a function for handling a click on an individual forecast
 *   in the list of forecast items managed by this adapter.  When the item is clicked, a
 *   representation of that item is passed into this function as a `ForecastPeriod` object.
 */
class ForecastAdapter(private val onClick: (ForecastPeriod) -> Unit)
    : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    var forecastPeriods: List<ForecastPeriod> = listOf()
    var forecastCity: ForecastCity? = null

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateForecast(forecast: FiveDayForecast?) {
        forecastPeriods = forecast?.periods ?: listOf()
        forecastCity = forecast?.city
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.forecastPeriods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.forecastPeriods[position], forecastCity)
    }

    class ViewHolder(itemView: View, val onClick: (ForecastPeriod) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val dateTV: TextView = itemView.findViewById(R.id.tv_date)
        private val timeTV: TextView = itemView.findViewById(R.id.tv_time)
        private val highTempTV: TextView = itemView.findViewById(R.id.tv_high_temp)
        private val lowTempTV: TextView = itemView.findViewById(R.id.tv_low_temp)
        private val iconIV: ImageView = itemView.findViewById(R.id.iv_forecast_icon)
        private val popTV: TextView = itemView.findViewById(R.id.tv_pop)

        private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)

        private lateinit var currentForecastPeriod: ForecastPeriod

        /*
         * Set up a click listener on this individual ViewHolder.  Call the provided onClick
         * function, passing the forecast item represented by this ViewHolder as an argument.
         */
        init {
            itemView.setOnClickListener {
                currentForecastPeriod.let(onClick)
            }
        }

        fun bind(forecastPeriod: ForecastPeriod, forecastCity: ForecastCity?) {
            currentForecastPeriod = forecastPeriod

            val ctx = itemView.context
            val date = openWeatherEpochToDate(forecastPeriod.epoch, forecastCity?.tzOffsetSec ?: 0)

            /*
             * Figure out the correct temperature and wind units to display for the current
             * setting of the units preference.
             */
            val units = sharedPrefs.getString(ctx.getString(R.string.pref_units_key), null)
            val tempUnitsDisplay = getTempUnitsDisplay(units, ctx)

            dateTV.text = ctx.getString(R.string.forecast_date, date)
            timeTV.text = ctx.getString(R.string.forecast_time, date)
            highTempTV.text = ctx.getString(
                R.string.forecast_temp,
                forecastPeriod.highTemp,
                tempUnitsDisplay
            )
            lowTempTV.text = ctx.getString(
                R.string.forecast_temp,
                forecastPeriod.lowTemp,
                tempUnitsDisplay
            )
            popTV.text = ctx.getString(R.string.forecast_pop, forecastPeriod.pop)

            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            Glide.with(ctx)
                .load(forecastPeriod.iconUrl)
                .into(iconIV)
        }
    }
}