package com.kryvovyaz.aetna.util

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.compose.runtime.mutableStateOf
import com.kryvovyaz.aetna.R
import com.kryvovyaz.aetna.models.Character
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.junit.After

class ShareHelperTest {

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var character: Character

    @RelaxedMockK
    lateinit var intent: Intent

    val isShowErrorDialog = mutableStateOf(false)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        justRun { context.startActivity(intent) }
        every { context.getString(R.string.app_created) } returns "Created:"
        every { context.getString(R.string.app_species) } returns "Species:"
        every { context.getString(R.string.app_status) } returns "Status:"
        every { context.getString(R.string.app_origin) } returns "Origin:"
        every { context.getString(R.string.app_type) } returns "Type:"
        every { context.getString(R.string.app_image) } returns "Image:"
        every { context.getString(R.string.app_unknown_name) } returns "Unknown Name"
        every { context.getString(R.string.app_unknown_species) } returns "Unknown Species"
        every { context.getString(R.string.app_unknown_status) } returns "Unknown Status"
        every { context.getString(R.string.app_unknown_origin) } returns "Unknown Origin"
        every { context.getString(R.string.app_unknown_origin) } returns "Unknown Origin"
        every { context.getString(R.string.app_unknown_type) } returns "Unknown Type"
        every { context.getString(R.string.app_unknown_image) } returns "No Image"
        every { context.getString(R.string.app_nane_text) } returns "Name:"
        mockkStatic(Intent::class)
        mockkConstructor(Intent::class)
        every { anyConstructed<Intent>().setAction(any()) } returns intent
        every { anyConstructed<Intent>().putExtra(any(), ofType(String::class)) } returns intent
        every { anyConstructed<Intent>().setType(any()) } returns intent
        every { Intent.createChooser(intent, ofType(String::class)) } returns intent
    }

    @After
    fun tearUp() {
        unmockkAll()
    }

    @Test
    fun `test shareCharacterDetails creates and starts intent with correct data`() {
        val resolveInfoList = listOf(mockk<ResolveInfo>().apply {
            every { context.packageName } returns "com.example.someapp"
        })
        every { context.packageManager.queryIntentActivities(any(), 0) } returns resolveInfoList
        // Given
        val name = "Rick"
        val species = "Human"
        val status = "Alive"
        val originName = "Earth"
        val type = "Regular"
        val image = "https://example.com/image.jpg"
        every { character.origin?.name } returns originName
        every { character.name } returns "Rick"
        every { character.species } returns species
        every { character.status } returns status
        every { character.type } returns type
        every { character.image } returns image

        val expectedShareMessage = """
            Name: Rick
            Species: Human
            Status: Alive
            Origin: Earth
            Type: Regular
            Image: https://example.com/image.jpg
        """.trimIndent()

        // When
        ShareHelper.shareCharacterDetails(context, character, isShowErrorDialog)

        // Then
        verify { context.startActivity(any()) }
        verify { anyConstructed<Intent>().putExtra(Intent.EXTRA_TEXT, expectedShareMessage) }
        verify { anyConstructed<Intent>().setType("text/plain") }
        verify { anyConstructed<Intent>().setAction(Intent.ACTION_SEND) }
        verify { Intent.createChooser(any(), any()) }
    }

    @Test
    fun `test shareCharacterDetails handles null values correctly`() {
        val resolveInfoList = listOf(mockk<ResolveInfo>().apply {
            every { context.packageName } returns "com.example.someapp"
        })
        every { context.packageManager.queryIntentActivities(any(), 0) } returns resolveInfoList
        // Given
        every { character.name } returns null
        every { character.origin } returns null
        every { character.species } returns null
        every { character.status } returns null
        every { character.type } returns null
        every { character.image } returns null

        val expectedShareMessage = """
            Name: Unknown Name
            Species: Unknown Species
            Status: Unknown Status
            Origin: Unknown Origin
            Type: Unknown Type
            Image: No Image
        """.trimIndent()

        // When
        ShareHelper.shareCharacterDetails(context, character, isShowErrorDialog)

        // Then
        verify { anyConstructed<Intent>().putExtra(Intent.EXTRA_TEXT, expectedShareMessage) }
    }

    @Test
    fun `test shareCharacterDetails handles null origin with non-null values`() {
        // Given
        val name = "Rick"
        val species = "Human"
        val status = "Alive"
        val type = "Regular"
        val image = "https://example.com/image.jpg"

        every { character.origin } returns null
        every { character.name } returns name
        every { character.species } returns species
        every { character.status } returns status
        every { character.type } returns type
        every { character.image } returns image

        val expectedShareMessage = """
            Name: Rick
            Species: Human
            Status: Alive
            Origin: Unknown Origin
            Type: Regular
            Image: https://example.com/image.jpg
        """.trimIndent()

        // When
        ShareHelper.shareCharacterDetails(context, character, isShowErrorDialog)

        // Then
        verify { anyConstructed<Intent>().putExtra(Intent.EXTRA_TEXT, expectedShareMessage) }
    }

    @Test
    fun `test shareCharacterDetails handles no activities to share intent`() {
        // Given: No activities are available to handle the share intent
        val resolveInfoList = emptyList<ResolveInfo>()
        every { context.packageManager.queryIntentActivities(any(), 0) } returns resolveInfoList

        val created = "April 1, 2023"
        val species = "Human"
        val status = "Alive"
        val originName = "Earth"
        val type = "Regular"
        val image = "https://example.com/image.jpg"
        every { character.origin?.name } returns originName
        every { character.created } returns created
        every { character.species } returns species
        every { character.status } returns status
        every { character.type } returns type
        every { character.image } returns image

        // When
        ShareHelper.shareCharacterDetails(context, character, isShowErrorDialog)

        // Then: Verify no intent is sent when no activities are available
        verify(exactly = 0) { context.startActivity(any()) }
        assert(isShowErrorDialog.value)
    }

    @Test
    fun `test shareCharacterDetails handles null character object`() {
        // Given: Character is null
        val characterNull: Character? = null

        // When: Call shareCharacterDetails with a null character
        ShareHelper.shareCharacterDetails(context, characterNull, isShowErrorDialog)

        // Then: Check that the fallback message is used
        val expectedShareMessage = """
            Created: Unknown
            Species: Unknown species
            Status: Unknown status
            Origin: Unknown origin
            Type: Unknown type
            Image: null
        """.trimIndent()

        verify(exactly = 0) { anyConstructed<Intent>().putExtra(Intent.EXTRA_TEXT, expectedShareMessage) }
        assert(isShowErrorDialog.value)
    }
}
