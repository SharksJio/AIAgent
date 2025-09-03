package com.sharksjio.aiagent

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sharksjio.aiagent.agents.*
import com.sharksjio.aiagent.core.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented tests for the AI Agent system
 */
@RunWith(AndroidJUnit4::class)
class AIAgentInstrumentedTest {
    
    private lateinit var agentManager: AgentManager
    private lateinit var contentFilter: ContentFilter
    private lateinit var responseProcessor: ResponseProcessor
    
    @Before
    fun setup() {
        agentManager = AgentManager()
        contentFilter = ContentFilter()
        responseProcessor = ResponseProcessor()
        
        // Register test agents
        agentManager.registerAgent(TextProcessorAgent())
        agentManager.registerAgent(ContentAnalyzerAgent())
        agentManager.registerAgent(QuestionAnswerAgent())
    }
    
    @Test
    fun testAgentRegistration() {
        val agents = agentManager.getAllAgents()
        assertEquals("Should have 3 registered agents", 3, agents.size)
        
        val agentIds = agents.map { it.config.id }
        assertTrue("Should contain text processor", agentIds.contains("text_processor_001"))
        assertTrue("Should contain content analyzer", agentIds.contains("content_analyzer_001"))
        assertTrue("Should contain Q&A agent", agentIds.contains("qa_agent_001"))
    }
    
    @Test
    fun testTopicDetection() {
        // Test technology topic detection
        val techTopic = contentFilter.detectTopic("I need help with programming and software development")
        assertEquals("Should detect technology topic", TopicCategory.TECHNOLOGY, techTopic)
        
        // Test business topic detection
        val businessTopic = contentFilter.detectTopic("What are the market trends and revenue strategies?")
        assertEquals("Should detect business topic", TopicCategory.BUSINESS, businessTopic)
        
        // Test health topic detection
        val healthTopic = contentFilter.detectTopic("I have questions about medical treatment and wellness")
        assertEquals("Should detect health topic", TopicCategory.HEALTH, healthTopic)
    }
    
    @Test
    fun testContentFiltering() {
        // Test appropriate content
        assertTrue("Should approve appropriate content", 
            contentFilter.isContentAppropriate("This is a normal question about technology"))
        
        // Test inappropriate content
        assertFalse("Should reject inappropriate content",
            contentFilter.isContentAppropriate("This is spam content that is inappropriate"))
    }
    
    @Test
    fun testSingleAgentProcessing() = runBlocking {
        val request = AgentRequest(
            id = UUID.randomUUID().toString(),
            content = "What is artificial intelligence?",
            topic = TopicCategory.TECHNOLOGY
        )
        
        val response = agentManager.processRequest(request)
        
        assertNotNull("Response should not be null", response)
        assertTrue("Response should have content", response.content.isNotBlank())
        assertTrue("Confidence should be between 0 and 1", 
            response.confidence >= 0.0 && response.confidence <= 1.0)
        assertEquals("Topic should match request", TopicCategory.TECHNOLOGY, response.topic)
    }
    
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("Package name should match", "com.sharksjio.aiagent", appContext.packageName)
    }
}