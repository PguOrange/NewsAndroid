package com.example.newsandroid.ui.everything

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.MainActivity
import com.example.newsandroid.MainActivity.FragmentRefreshListener
import com.example.newsandroid.MyApp
import com.example.newsandroid.R
import com.example.newsandroid.adapter.NewsAdapterPagination
import com.example.newsandroid.adapter.PaginationListener
import com.example.newsandroid.adapter.PaginationListener.Companion.PAGE_START
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.enums.NewsListContainer
import com.example.newsandroid.enums.SortBy
import com.example.newsandroid.factory.ViewModelFactory
import kotlinx.android.synthetic.main.everything_fragment.*
import kotlinx.android.synthetic.main.layout_custom_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


enum class Option {FROMDATE, TODATE}
class EverythingFragment : Fragment() {

    private var dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    private var dateFormatterUS = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val adapter = NewsAdapterPagination(ArrayList(), NewsListContainer.EVERYTHING)


    private val everythingViewModel: EverythingViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            ViewModelFactory(application)
        ViewModelProviders.of(
            this, viewModelFactory).get(EverythingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.everything_fragment, container, false)



        everythingViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    NewsApiStatus.LOADING -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.loading_animation)
                    }
                    NewsApiStatus.ERROR -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.ic_connection_error)
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.ic_connection_error)
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_WITH_CACHE  -> {
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API_WITH_CACHE -> {
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        status_image_everything.visibility = View.GONE
                    }
                    NewsApiStatus.DONE_EMPTY -> {
                        status_image_everything.visibility = View.GONE
                        Toast.makeText(activity, "Aucune News trouvée", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.END_PAGE -> {
                        status_image_everything.visibility = View.GONE
                        everythingViewModel.isLastPage = true
                        adapter.removeLoading()
                    }
                    else -> {}
                }
            }
        })

        everythingViewModel.property.observe(viewLifecycleOwner, Observer {
            if (everythingViewModel.currentPage == 1) {
                adapter.replaceItems(it)
                searchView.queryHint = everythingViewModel.currentQuery
                if (everythingViewModel.size>=everythingViewModel.apiNewsPerPage) adapter.addLoading()
            }else{
                if (everythingViewModel.currentPage != PAGE_START) adapter.removeLoading()
                adapter.addItems(it.takeLast((everythingViewModel.currentPage-1)*everythingViewModel.apiNewsPerPage))
                if (!everythingViewModel.isLastPage) {
                    adapter.addLoading()
                }
                everythingViewModel.isLoading = false

            }
        })

        (activity as MainActivity?)!!.setFragmentRefreshListener(object :
            FragmentRefreshListener {
            override fun onRefresh() {

                everythingViewModel.getEverythingProperties()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh_layout_everything.setOnRefreshListener {
            everythingViewModel.itemCount = 0
            everythingViewModel.currentPage = PAGE_START
            everythingViewModel.isLastPage = false
            everythingViewModel.getEverythingProperties()
            swipe_refresh_layout_everything.isRefreshing = false
        }

        filter_button_everything.setOnClickListener{
            showDialog()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                everythingViewModel.onQueryChanged(query)
                everythingViewModel.getEverythingProperties()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        everything_news_list.layoutManager = layoutManager
        everything_news_list.adapter = adapter
        /*
        everything_news_list.apply {
            this.adapter = adapter
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        */

        everything_news_list.addOnScrollListener(object : PaginationListener(layoutManager){
            override fun loadMoreItems() {
                everythingViewModel.isLoading = true
                everythingViewModel.currentPage++
                everythingViewModel.getEverythingProperties()
                //adapter.addItems(it)


            }

            override fun isLastPage(): Boolean {
                return everythingViewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return everythingViewModel.isLoading
            }

        })

    }



    private fun showDialog() {
        val alertLayout: View = layoutInflater.inflate(R.layout.layout_custom_dialog, null)
        val alert = AlertDialog.Builder(context)
        val sortBy : List<SortBy> = listOf(
            SortBy.Relevancy,
            SortBy.PublishedAt,
            SortBy.Popularity
        )
        var sortByFR : List<String> = listOf(
            SortBy.Relevancy.displayFR,
            SortBy.PublishedAt.displayFR,
            SortBy.Popularity.displayFR
        )
        var sortByUS : List<SortBy> = listOf(
            SortBy.Relevancy,
            SortBy.PublishedAt,
            SortBy.Popularity
        )
        /*
        if (MyApp.globalLanguage.language=="FR") {
            alert.setTitle("Filtre de recherche")
            alert.setTitle(getString(R.string.titleFR))
            alertLayout.sort_by.text ="Trier par :"
            alertLayout.from_date.text = "Date début:"
            alertLayout.to_date.text = "Date fin:"
        }else {
            alert.setTitle("Search filter")
            alertLayout.sort_by.text ="Sort by:"
            alertLayout.from_date.text = "From date:"
            alertLayout.to_date.text = "To date:"
        }

         */

        alert.setTitle(getString(R.string.title))
        alertLayout.sort_by.text = getString(R.string.sort_by)
        alertLayout.from_date.text = getString(R.string.from_date)
        alertLayout.to_date.text = getString(R.string.to_date)

        alert.setView(alertLayout)
        alert.setCancelable(false)
        /*
        val aa = if (MyApp.globalLanguage.language=="FR")
         ArrayAdapter(context!!, android.R.layout.simple_spinner_item, sortByFR)
        else
        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, sortByUS)

         */

        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sort_by_array))

        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        alertLayout.sp_sort!!.adapter = aa

        if (everythingViewModel.currentFromDate=="") alertLayout.button_delete_date.visibility = View.GONE
        if (everythingViewModel.currentToDate=="") alertLayout.button_delete_to_date.visibility = View.GONE
        alertLayout.sp_sort.setSelection(everythingViewModel.currentSortPosition)

        alertLayout.date_text.text = everythingViewModel.currentFromDateFR
        alertLayout.to_date_text.text = everythingViewModel.currentToDateFR

        alert.setNegativeButton(
            /*
            if (MyApp.globalLanguage.language=="FR")
                "Annuler"
            else
                "Cancel"

             */
            getString(R.string.cancel)
        ) { dialog, which ->

        }

        alert.setPositiveButton(
            "OK"
        ) { dialog, which ->
            val sortPos = alertLayout.sp_sort.selectedItemPosition

            if (alertLayout.date_text.text.toString() != "--/--/----") {
                if (everythingViewModel.tmpDateUsed)
                everythingViewModel.onFromDateChanged(
                    dateFormatterUS.format(everythingViewModel.tmpDate.time),
                    dateFormatter.format(everythingViewModel.tmpDate.time)
                )

            }
            else
                everythingViewModel.onFromDateCanceled()

            if (alertLayout.to_date_text.text.toString() != "--/--/----") {
                if (everythingViewModel.tmpToDateUsed)
                    everythingViewModel.onToDateChanged(
                        dateFormatterUS.format(everythingViewModel.tmpToDate.time),
                        dateFormatter.format(everythingViewModel.tmpToDate.time)
                    )

            }
            else
                everythingViewModel.onToDateCanceled()

            //everythingViewModel.onFilterChanged(language, transformSpinnerStringToParametersApi(sort), languagePos, sortPos)
            Log.d("SelectedItemPosition", alertLayout.sp_sort.selectedItemPosition.toString() + " "+ sortBy[alertLayout.sp_sort.selectedItemPosition].toString() )
            everythingViewModel.onFilterChanged(sortBy[alertLayout.sp_sort.selectedItemPosition].toString(), sortPos)
            everythingViewModel.getEverythingProperties()

        }

        alert.setNeutralButton(
/*
            if (MyApp.globalLanguage.language=="FR")
                "Réinitialiser"
            else
                "Reset"

 */
            getString(R.string.reset)
        ) { dialog, which ->
            everythingViewModel.onFilterReset()
            everythingViewModel.getEverythingProperties()
        }

        val dialog = alert.create()

        alertLayout.button_date.setOnClickListener {
            setDateTimeField(alertLayout, alertLayout.date_text, alertLayout.button_delete_date, Option.FROMDATE)
        }

        alertLayout.button_delete_date.setOnClickListener {
            alertLayout.date_text.text = "--/--/----"
            alertLayout.button_delete_date.visibility = View.GONE
            //everythingViewModel.tmpDateUsed = false
        }

        alertLayout.button_to_date.setOnClickListener {
            setDateTimeField(alertLayout, alertLayout.to_date_text, alertLayout.button_delete_to_date, Option.TODATE)
        }

        alertLayout.button_delete_to_date.setOnClickListener {
            alertLayout.to_date_text.text = "--/--/----"
            alertLayout.button_delete_to_date.visibility = View.GONE
            //everythingViewModel.tmpToDateUsed = false
        }



        dialog.show()
    }

    private fun setDateTimeField(alertLayout: View, text: TextView, button: ImageButton, option: Option){
        val newCalendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context!!,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                text.text = if (MyApp.globalLanguage.language=="FR")
                        dateFormatter.format(newDate.time)
                    else
                        dateFormatterUS.format(newDate.time)
                if (option==Option.FROMDATE) everythingViewModel.onDateSelected(newDate.time)
                else everythingViewModel.onToDateSelected(newDate.time)
                button.visibility = View.VISIBLE
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        when (option) {
            Option.FROMDATE -> {
                when {
                    alertLayout.to_date_text.text.toString() == "--/--/----" -> datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                    everythingViewModel.tmpToDateUsed -> datePickerDialog.datePicker.maxDate = everythingViewModel.tmpToDate.time
                    everythingViewModel.currentToDate == "" -> datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                    else -> datePickerDialog.datePicker.maxDate =
                        dateFormatterUS.parse(everythingViewModel.currentToDate).time
                }
            }
            else -> {
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                if (alertLayout.date_text.text.toString() != "--/--/----") {
                    when {
                        everythingViewModel.tmpDateUsed -> datePickerDialog.datePicker.minDate =
                            everythingViewModel.tmpDate.time
                        everythingViewModel.currentFromDate != "" -> datePickerDialog.datePicker.minDate =
                            dateFormatterUS.parse(everythingViewModel.currentFromDate).time
                    }
                }
            }
        }

        datePickerDialog.show()
    }
}