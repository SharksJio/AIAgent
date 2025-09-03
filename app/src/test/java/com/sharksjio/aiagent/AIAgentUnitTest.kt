package com.sharksjio.aiagent

import com.sharksjio.aiagent.core.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Unit tests for core AI Agent functionality
 */
class AIAgentUnitTest {
    
    private lateinit var contentFilter: ContentFilter
    private lateinit var responseProcessor: ResponseProcessor
    
    @Before
    fun setup() {
        contentFilter = ContentFilter()
        responseProcessor = ResponseProcessor()
    }
    
    @Test
    fun testTopicDetection() {
        // Test technology keywords
        assertEquals(TopicCategory.TECHNOLOGY, 
            contentFilter.detectTopic("I need help with programming and AI"))
        
        // Test business keywords  
        assertEquals(TopicCategory.BUSINESS,
            contentFilter.detectTopic("What are the market strategies for revenue growth?"))
        
        // Test health keywords
        assertEquals(TopicCategory.HEALTH,
            contentFilter.detectTopic("I have questions about medical treatment"))
        
        // Test general fallback
        assertEquals(TopicCategory.GENERAL,
            contentFilter.detectTopic("Random content without specific keywords"))
    }
    
    @Test
    fun testContentFiltering() {
        // Appropriate content
        assertTrue(contentFilter.isContentAppropriate("This is a normal question"))
        assertTrue(contentFilter.isContentAppropriate("How does AI work?"))
        
        // Inappropriate content
        assertFalse(contentFilter.isContentAppropriate("This is spam content"))
        assertFalse(contentFilter.isContentAppropriate("Inappropriate message here"))
    }
    
    @Test
    fun testRelevanceScoring() {
        val techContent = "AI and machine learning algorithms"
        val relevanceScore = contentFilter.calculateRelevanceScore(techContent, TopicCategory.TECHNOLOGY)
        
        assertTrue("Should have positive relevance for tech content", relevanceScore > 0.0)
        assertTrue("Should have reasonable relevance score", relevanceScore <= 1.0)
    }
    
    @Test
    fun testResponseValidation() {
        val validResponse = AgentResponse(
            id = "test",
            agentId = "test_agent",
            content = "Valid response content",
            confidence = 0.85,
            topic = TopicCategory.GENERAL,
            processingTime = 100L
        )
        
        assertTrue("Valid response should pass validation", 
            responseProcessor.validateResponse(validResponse))
        
        val invalidResponse = AgentResponse(
            id = "test",
            agentId = "test_agent", 
            content = "",
            confidence = 1.5, // Invalid confidence
            topic = TopicCategory.GENERAL,
            processingTime = 100L
        )
        
        assertFalse("Invalid response should fail validation",
            responseProcessor.validateResponse(invalidResponse))
    }
    
    @Test
    fun testAgentConfig() {
        val config = AgentConfig(
            id = "test_agent",
            name = "Test Agent",
            type = AgentType.CUSTOM,
            supportedTopics = setOf(TopicCategory.TECHNOLOGY),
            priority = 5
        )
        
        assertEquals("test_agent", config.id)
        assertEquals("Test Agent", config.name)
        assertEquals(AgentType.CUSTOM, config.type)
        assertTrue(config.supportedTopics.contains(TopicCategory.TECHNOLOGY))
        assertEquals(5, config.priority)
        assertTrue(config.isEnabled) // Default should be true
    }
}