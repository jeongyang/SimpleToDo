package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.apache.commons.io.FileUtils.readLines
import org.apache.commons.io.IOUtils.readLines
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTasks.removeAt(position)
                // Notify the adapter
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // listOfTasks.add("Do laundry")
        // listOfTasks.add("Go for a walk")

        loadItems()  // Populate items

        // Look up the recycleView in layout
        val recylerView = findViewById<RecyclerView>(R.id.recycleView)
        // Create an adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recylerView.adapter = adapter
        // Set layout manager to position the items
        recylerView.layoutManager = LinearLayoutManager(this);

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        // Set up a button so that the user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener {
            // Grab a text the user has inputted
            val userInputtedTask = inputTextField.text.toString()

            // Add the text to the list of tasks
            listOfTasks.add(userInputtedTask)
            // Notify the adapter of the data inputted
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // Reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // Save data by writing and reading from a file

    // Create a method to ge the file we need
    fun getDataFile(): File {
        // Every item in a every line in a file
        return File(filesDir, "data.txt")
    }

    // Load items by reading every line from the file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    // Save items by writing
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}