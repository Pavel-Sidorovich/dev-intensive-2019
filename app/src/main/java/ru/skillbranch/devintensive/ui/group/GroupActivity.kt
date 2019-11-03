package ru.skillbranch.devintensive.ui.group

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.android.synthetic.main.activity_group.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.ui.custom.CircleImageView
import ru.skillbranch.devintensive.ui.custom.MyDividerItemDecorator
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.GroupViewModel
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.annotation.Nullable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target


class GroupActivity : AppCompatActivity() {

    private lateinit var usersAdapter: UserAdapter
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.AppTheme)

        Log.d("M_GroupActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setTitle(R.string.create_group)
        setContentView(R.layout.activity_group)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        usersAdapter = UserAdapter { viewModel.handleSelectedItem(it.id) }
        val divider = MyDividerItemDecorator(
            resources.getDrawable(
                R.drawable.divider,
                theme
            )
        )//DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        with(rv_user_list) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            viewModel.handleCreateGroup()
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        viewModel.getUsersData().observe(this, Observer { usersAdapter.updateData(it) })
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
//        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun toggleFab(isShow: Boolean) {
        if (isShow) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            text = user.fullName
            if (user.avatar == null) {
                chipIcon = BitmapDrawable(resources, drawChipIcon(this.chipIconSize, drawInitials(user.initials ?: "", context, (chipIconSize).toInt())))
            } else {
                Glide.with(this)
                    .load(Uri.parse(user.avatar))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            chipIcon = resources.getDrawable(R.drawable.avatar_default, theme)
                            chipIcon = BitmapDrawable(resources, drawChipIcon(this@apply.chipIconSize, drawInitials(user.initials ?: "", context, (chipIconSize).toInt())))
//                            chipIcon = drawInitials(user.initials, context)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            chipIcon = resource
                            chipIcon = BitmapDrawable(resources, drawChipIcon(this@apply.chipIconSize * 2.5f, drawableToBitmap(resource)!!))
                            return false
                        }
                    }).preload()
            }


            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint =
                ColorStateList.valueOf(Utils.getColorFromAttr(R.attr.colorCloseChip, theme))
            chipBackgroundColor =
                ColorStateList.valueOf(Utils.getColorFromAttr(R.attr.colorBgChip, theme))
            setTextColor(Color.WHITE)
        }
        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? =
        when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> try {
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    private fun drawChipIcon(chipIconSize: Float, myBitmap: Bitmap): Bitmap {
        val paint = Paint()
        val canvas = Canvas()
//        val chipIconSize = chip.chipIconSize
        val bitmap =
            Bitmap.createBitmap(chipIconSize.toInt(), chipIconSize.toInt(), Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        canvas.drawCircle(chipIconSize / 2f, chipIconSize / 2f, chipIconSize / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(myBitmap, 0f, 0f, paint)
        return bitmap
    }

    private fun resize(image: Drawable, chip: Chip) : Drawable  {
        val chipIconSize = 20f//chip.chipIconSize / 2f
    val b = (image as BitmapDrawable).bitmap
    val bitmapResized = Bitmap.createScaledBitmap(b, chipIconSize.toInt(), chipIconSize.toInt(), true)
    return BitmapDrawable(resources, bitmapResized)
}

    private fun updateChips(listUsers: List<UserItem>) {
        chip_group.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE

        val users = listUsers
            .associate { user -> user.id to user }
            .toMutableMap()

        val views = chip_group.children.associate { view -> view.tag to view }

        for ((k, v) in views) {
            if (!users.containsKey(k)) {
                chip_group.removeView(v)
            } else {
                users.remove(k)
            }
        }

        users.forEach { (_, v) -> addChipToGroup(v) }
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun drawInitials(text: String, myContext: Context, size: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val mPaint = Paint()
        val mTextBoundRect = Rect()


        val centerX = size / 2
        val centerY = size / 2

//        val value = TypedValue()
//        myContext.theme.resolveAttribute(R.attr.colorAccent, value, true)
//        canvas.drawColor(value.data)

        canvas.drawColor(Utils.getColorRandom(text))

        // Рисуем текст
        mPaint.color = Color.WHITE
        mPaint.textSize = myContext.resources.getDimension(R.dimen.avatar_initials_12)

        // Подсчитаем размер текста
        mPaint.getTextBounds(text, 0, text.length, mTextBoundRect)
        // Используем measureText для измерения ширины
        val mTextWidth = mPaint.measureText(text)
        val mTextHeight = mTextBoundRect.height()

        canvas.drawText(
            text,
            centerX - (mTextWidth / 2f),
            centerY + (mTextHeight / 2f),
            mPaint
        )

        return bitmap
    }
}
