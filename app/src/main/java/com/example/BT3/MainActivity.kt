package com.example.bai1

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var addressHelper: AddressHelper
    private lateinit var spTinhThanh: Spinner
    private lateinit var spQuanHuyen: Spinner
    private lateinit var spPhuongXa: Spinner
    private lateinit var tvSelectedDate: TextView
    private var selectedDate: String? = null // Biến để lưu ngày đã chọn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addressHelper = AddressHelper(resources)
        // Khai báo các thành phần giao diện
        val etMSSV = findViewById<EditText>(R.id.etMSSV)
        val etHoTen = findViewById<EditText>(R.id.etHoTen)
        val rbMale = findViewById<RadioButton>(R.id.radioNam)
        val rbFemale = findViewById<RadioButton>(R.id.radioNu)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etSdt)
        val btnShowCalendar = findViewById<Button>(R.id.btnChonNgay)
        val cbTheThao = findViewById<CheckBox>(R.id.cbTheThao)
        val cbDienAnh = findViewById<CheckBox>(R.id.cbDienAnh)
        val cbAmNhac = findViewById<CheckBox>(R.id.cbAmNhac)
        val cbAcceptTerms = findViewById<CheckBox>(R.id.cbAcceptTerms)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        spTinhThanh = findViewById(R.id.spTinhThanh)
        spQuanHuyen = findViewById(R.id.spQuanHuyen)
        spPhuongXa = findViewById(R.id.spPhuongXa)
        setupProvinceSpinner()
        // Hiển thị Calendar khi nhấn nút chọn ngày sinh

        btnShowCalendar.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_calendar)

            val calendarViewDialog = dialog.findViewById<CalendarView>(R.id.calendarViewDialog)
            val btnClose = dialog.findViewById<Button>(R.id.btnClose)

            btnClose.setOnClickListener {
                dialog.dismiss() // Đóng dialog khi nhấn nút Close
            }
            calendarViewDialog.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                Toast.makeText(this, "Ngày sinh đã chọn: $selectedDate", Toast.LENGTH_SHORT).show()
                tvSelectedDate.text = "Ngày sinh: $selectedDate"
                dialog.dismiss()
            }
            dialog.show() // Hiển thị dialog
        }
        // Xử lý khi nhấn nút Submit
        btnSubmit.setOnClickListener {
            val mssv = etMSSV.text.toString()
            val hoTen = etHoTen.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val gender = if (rbMale.isChecked) "Nam" else if (rbFemale.isChecked) "Nữ" else ""
            val tinhThanh = spTinhThanh.selectedItem.toString()
            val termsAccepted = cbAcceptTerms.isChecked
            val hobbies = mutableListOf<String>()

            if (cbTheThao.isChecked) hobbies.add("Thể thao")
            if (cbDienAnh.isChecked) hobbies.add("Điện ảnh")
            if (cbAmNhac.isChecked) hobbies.add("Âm nhạc")

            // Kiểm tra dữ liệu hợp lệ
            if (mssv.isEmpty() || hoTen.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty()  || selectedDate.isNullOrEmpty() || !termsAccepted) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và đồng ý điều khoản", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupProvinceSpinner() {
        val provinces = addressHelper.getProvinces()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTinhThanh.adapter = adapter

        // Lắng nghe sự kiện chọn tỉnh
        spTinhThanh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                setupDistrictSpinner(selectedProvince)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupDistrictSpinner(province: String) {
        val districts = addressHelper.getDistricts(province)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spQuanHuyen.adapter = adapter

        // Lắng nghe sự kiện chọn huyện
        spQuanHuyen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDistrict = districts[position]
                setupWardSpinner(province, selectedDistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupWardSpinner(province: String, district: String) {
        val wards = addressHelper.getWards(province, district)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wards)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPhuongXa.adapter = adapter
    }

}
