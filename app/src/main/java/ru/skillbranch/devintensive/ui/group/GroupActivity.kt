package ru.skillbranch.devintensive.ui.group

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_group.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.ui.custom.MyDividerItemDecorator
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private lateinit var usersAdapter: UserAdapter
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

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
        return if(item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        usersAdapter = UserAdapter { viewModel.handleSelectedItem(it.id) }
        val divider = MyDividerItemDecorator(resources.getDrawable(R.drawable.divider, theme))//DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

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
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun toggleFab(isShow: Boolean) {
        if(isShow) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            text = user.fullName
            chipIcon = resources.getDrawable(R.drawable.avatar_default, theme)

//            if(user.avatar == null){
////                Glide.with(chip_group)
////                    .clear(chipIcon)
//                chipIcon = drawInitials("jj", this.context).toDrawable(resources)
////                chipIcon.draw(drawInitials("jjl", this.context))
////                iv_avatar_single.setImageBitmap(drawInitials(user.initials, chipIcon.context))
//            } else {
//                Glide.with(context)
//                    .load(user.avatar)
//                    .into(this.)
//            }

            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(Utils.getColorFromAttr(R.attr.colorCloseChip, theme))
            chipBackgroundColor = ColorStateList.valueOf(Utils.getColorFromAttr(R.attr.colorBgChip, theme))
            setTextColor(Color.WHITE)
        }
        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
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

    private fun drawInitials(text : String, myContext : Context) : Bitmap {
        val size = myContext.resources.getDimension(R.dimen.avatar_item_size).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val mPaint = Paint()
        val mTextBoundRect = Rect()


        val centerX = size / 2
        val centerY = size / 2

        val value = TypedValue()
        myContext.theme.resolveAttribute(R.attr.colorAccent, value, true)
        canvas.drawColor(value.data)
        // Рисуем текст
        mPaint.color = Color.WHITE
        mPaint.textSize = myContext.resources.getDimension(R.dimen.avatar_initials_20)

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
