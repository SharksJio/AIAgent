package com.sharksjio.aiagent.agents

import com.sharksjio.aiagent.core.*
import kotlinx.coroutines.delay

/**
 * Mock text processing agent for demonstration
 */
class TextProcessorAgent : BaseAIAgent(
    AgentConfig(
        id = "text_processor_001",
        name = "Advanced Text Processor",
        type = AgentType.TEXT_PROCESSOR,
        supportedTopics = setOf(TopicCategory.GENERAL, TopicCategory.TECHNOLOGY, TopicCategory.EDUCATION),
        priority = 10
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        
        // Simulate processing time
        delay(500)
        
        val processedContent = when (request.topic) {
            TopicCategory.TECHNOLOGY -> "Technical Analysis: ${request.content}. This appears to be technology-related content that requires specialized processing."
            TopicCategory.EDUCATION -> "Educational Insight: ${request.content}. Here's a learning-focused perspective on your query."
            else -> "Processed Content: ${request.content}. I've analyzed your text and provided this enhanced response."
        }
        
        return createResponse(
            requestId = request.id,
            content = processedContent,
            confidence = 0.85,
            topic = request.topic,
            startTime = startTime
        )
    }
}

/**
 * Mock content analysis agent
 */
class ContentAnalyzerAgent : BaseAIAgent(
    AgentConfig(
        id = "content_analyzer_001",
        name = "Content Analyzer Pro",
        type = AgentType.CONTENT_ANALYZER,
        supportedTopics = setOf(TopicCategory.BUSINESS, TopicCategory.SCIENCE, TopicCategory.GENERAL),
        priority = 8
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        
        delay(700)
        
        val analysisResult = "Content Analysis Report:\n" +
                "- Topic: ${request.topic}\n" +
                "- Word Count: ${request.content.split(" ").size}\n" +
                "- Sentiment: Positive\n" +
                "- Key Insights: This content shows good structure and relevance to the ${request.topic} domain.\n" +
                "- Recommendations: Consider expanding on the main points for better clarity."
        
        return createResponse(
            requestId = request.id,
            content = analysisResult,
            confidence = 0.92,
            topic = request.topic,
            startTime = startTime
        )
    }
}

/**
 * Mock question answering agent
 */
class QuestionAnswerAgent : BaseAIAgent(
    AgentConfig(
        id = "qa_agent_001",
        name = "Q&A Specialist",
        type = AgentType.QUESTION_ANSWERER,
        supportedTopics = TopicCategory.values().toSet(),
        priority = 9
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        
        delay(600)
        
        val answer = if (request.content.contains("?")) {
            "Answer: Based on your question about ${request.topic}, here's what I found: " +
                    "This is a comprehensive response that addresses your specific inquiry. " +
                    "The information is tailored to the ${request.topic} domain and provides actionable insights."
        } else {
            "Information: While this wasn't phrased as a question, I can provide relevant information about ${request.topic}: " +
                    "Your content relates to important aspects of this field. Here are some key points to consider."
        }
        
        return createResponse(
            requestId = request.id,
            content = answer,
            confidence = 0.88,
            topic = request.topic,
            startTime = startTime
        )
    }
}