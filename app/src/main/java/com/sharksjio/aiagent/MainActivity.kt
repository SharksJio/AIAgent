package com.sharksjio.aiagent

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sharksjio.aiagent.agents.*
import com.sharksjio.aiagent.core.*
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var agentManager: AgentManager
    private lateinit var responseProcessor: ResponseProcessor
    
    // UI Elements
    private lateinit var titleText: TextView
    private lateinit var agentStatusText: TextView
    private lateinit var topicSpinner: Spinner
    private lateinit var multipleAgentsCheckbox: CheckBox
    private lateinit var responseText: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var clearButton: Button
    private lateinit var settingsButton: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        initializeAgents()
        setupUI()
    }
    
    private fun initializeViews() {
        titleText = findViewById(R.id.titleText)
        agentStatusText = findViewById(R.id.agentStatusText)
        topicSpinner = findViewById(R.id.topicSpinner)
        multipleAgentsCheckbox = findViewById(R.id.multipleAgentsCheckbox)
        responseText = findViewById(R.id.responseText)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        clearButton = findViewById(R.id.clearButton)
        settingsButton = findViewById(R.id.settingsButton)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun initializeAgents() {
        agentManager = AgentManager()
        responseProcessor = ResponseProcessor()
        
        // Register sample agents
        agentManager.registerAgent(TextProcessorAgent())
        agentManager.registerAgent(ContentAnalyzerAgent())
        agentManager.registerAgent(QuestionAnswerAgent())
        
        // Register specialized agents
        agentManager.registerAgent(HealthAgent())
        agentManager.registerAgent(BusinessAgent())
        agentManager.registerAgent(RemoteAPIAgent())
        
        updateAgentStatus()
    }
    
    private fun setupUI() {
        // Setup topic spinner
        val topics = TopicCategory.values().map { it.name.replace("_", " ") }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, topics)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        topicSpinner.adapter = adapter
        
        // Set default to GENERAL
        topicSpinner.setSelection(TopicCategory.GENERAL.ordinal)
        
        // Setup button listeners
        sendButton.setOnClickListener { processmessage() }
        clearButton.setOnClickListener { clearResponse() }
        settingsButton.setOnClickListener { showAgentSettings() }
        
        // Handle enter key in message input
        messageInput.setOnEditorActionListener { _, _, _ ->
            processmessage()
            true
        }
    }
    
    private fun processmessage() {
        val message = messageInput.text.toString().trim()
        if (message.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_message), Toast.LENGTH_SHORT).show()
            return
        }
        
        val selectedTopic = TopicCategory.values()[topicSpinner.selectedItemPosition]
        val useMultipleAgents = multipleAgentsCheckbox.isChecked
        
        showLoading(true)
        messageInput.text.clear()
        
        lifecycleScope.launch {
            try {
                val request = AgentRequest(
                    id = UUID.randomUUID().toString(),
                    content = message,
                    topic = selectedTopic
                )
                
                if (useMultipleAgents) {
                    val responses = agentManager.processRequestWithMultipleAgents(request)
                    val aggregatedResponse = responseProcessor.aggregateResponses(responses)
                    displayResponse(aggregatedResponse)
                } else {
                    val response = agentManager.processRequest(request)
                    displayResponse(response)
                }
            } catch (e: Exception) {
                responseText.text = "Error processing request: ${e.message}"
            } finally {
                showLoading(false)
            }
        }
    }
    
    private fun displayResponse(response: AgentResponse) {
        val formattedResponse = if (response.metadata.containsKey("aggregated")) {
            response.content
        } else {
            responseProcessor.formatResponse(response)
        }
        
        responseText.text = formattedResponse
    }
    
    private fun clearResponse() {
        responseText.text = "Welcome to AI Agent Assistant!\n\nSelect a topic and enter your message below to get started. The system will automatically route your request to the most appropriate AI agents."
        messageInput.text.clear()
    }
    
    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        sendButton.isEnabled = !show
        sendButton.text = if (show) getString(R.string.processing) else getString(R.string.send)
    }
    
    private fun updateAgentStatus() {
        val activeAgents = agentManager.getAllAgents().filter { it.isAvailable() }
        agentStatusText.text = "Agents Ready: ${activeAgents.size} active"
    }
    
    private fun showAgentSettings() {
        val agents = agentManager.getAllAgents()
        val agentNames = agents.map { "${it.config.name} (${it.config.type})" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Available AI Agents")
            .setItems(agentNames) { _, which ->
                val agent = agents[which]
                showAgentDetails(agent)
            }
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showAgentDetails(agent: AIAgent) {
        val details = buildString {
            appendLine("Agent: ${agent.config.name}")
            appendLine("Type: ${agent.config.type}")
            appendLine("Priority: ${agent.config.priority}")
            appendLine("Status: ${if (agent.isAvailable()) "Active" else "Inactive"}")
            appendLine("Supported Topics:")
            agent.config.supportedTopics.forEach { topic ->
                appendLine("  • ${topic.name.replace("_", " ")}")
            }
        }
        
        AlertDialog.Builder(this)
            .setTitle("Agent Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }
}