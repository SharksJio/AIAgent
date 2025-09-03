package com.sharksjio.aiagent.agents

import com.sharksjio.aiagent.core.*
import kotlinx.coroutines.delay

/**
 * Health-focused AI agent for medical and wellness queries
 */
class HealthAgent : BaseAIAgent(
    AgentConfig(
        id = "health_agent_001",
        name = "Health & Wellness Advisor",
        type = AgentType.CUSTOM,
        supportedTopics = setOf(TopicCategory.HEALTH),
        priority = 9
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        delay(800)
        
        val healthAdvice = buildString {
            appendLine("🏥 Health & Wellness Analysis")
            appendLine("=" .repeat(35))
            appendLine()
            appendLine("Query: ${request.content}")
            appendLine()
            appendLine("Health Insights:")
            appendLine("• This appears to be a health-related inquiry")
            appendLine("• Always consult with healthcare professionals for medical advice")
            appendLine("• Consider lifestyle factors that may be relevant")
            appendLine("• Stay informed about evidence-based health practices")
            appendLine()
            appendLine("⚠️ Disclaimer: This is AI-generated information and should not replace professional medical advice.")
        }
        
        return createResponse(
            requestId = request.id,
            content = healthAdvice,
            confidence = 0.87,
            topic = request.topic,
            startTime = startTime
        )
    }
}

/**
 * Business-focused AI agent for enterprise and strategy queries
 */
class BusinessAgent : BaseAIAgent(
    AgentConfig(
        id = "business_agent_001",
        name = "Business Strategy Analyst",
        type = AgentType.CUSTOM,
        supportedTopics = setOf(TopicCategory.BUSINESS, TopicCategory.GENERAL),
        priority = 8
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        delay(600)
        
        val businessAnalysis = buildString {
            appendLine("📊 Business Analysis Report")
            appendLine("=" .repeat(35))
            appendLine()
            appendLine("Query: ${request.content}")
            appendLine()
            appendLine("Business Perspective:")
            appendLine("• Market Context: This query relates to current business trends")
            appendLine("• Strategic Implications: Consider long-term impact on operations")
            appendLine("• Risk Assessment: Evaluate potential challenges and opportunities")
            appendLine("• Competitive Analysis: Monitor industry best practices")
            appendLine()
            appendLine("Recommendations:")
            appendLine("✓ Conduct thorough market research")
            appendLine("✓ Develop a clear implementation strategy")
            appendLine("✓ Monitor key performance indicators")
            appendLine("✓ Engage stakeholders throughout the process")
        }
        
        return createResponse(
            requestId = request.id,
            content = businessAnalysis,
            confidence = 0.91,
            topic = request.topic,
            startTime = startTime
        )
    }
}

/**
 * Remote API agent that can integrate with external services
 */
class RemoteAPIAgent : BaseAIAgent(
    AgentConfig(
        id = "remote_api_agent_001",
        name = "External API Integrator",
        type = AgentType.CUSTOM,
        supportedTopics = TopicCategory.values().toSet(),
        priority = 5,
        endpoint = "https://api.example.com/v1/ai",
        apiKey = "demo_api_key"
    )
) {
    
    override suspend fun processRequest(request: AgentRequest): AgentResponse {
        val startTime = System.currentTimeMillis()
        
        // Simulate API call
        delay(1200)
        
        val apiResponse = buildString {
            appendLine("🌐 External API Integration Response")
            appendLine("=" .repeat(40))
            appendLine()
            appendLine("Endpoint: ${config.endpoint}")
            appendLine("Topic: ${request.topic}")
            appendLine("Processing Method: Remote API Call")
            appendLine()
            appendLine("Simulated API Response:")
            appendLine("This is a demonstration of how the AI Agent system can integrate")
            appendLine("with external APIs and services. In a real implementation, this")
            appendLine("agent would make HTTP requests to external AI services like:")
            appendLine()
            appendLine("• OpenAI GPT API")
            appendLine("• Google Cloud AI Platform")
            appendLine("• IBM Watson")
            appendLine("• Azure Cognitive Services")
            appendLine("• Hugging Face Inference API")
            appendLine()
            appendLine("The response would be processed and formatted according to the")
            appendLine("specific requirements of your application.")
        }
        
        return createResponse(
            requestId = request.id,
            content = apiResponse,
            confidence = 0.89,
            topic = request.topic,
            startTime = startTime
        )
    }
}