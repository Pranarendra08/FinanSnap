from flask import Flask, request, jsonify, render_template
import easyocr
import re
import os
from werkzeug.utils import secure_filename

app = Flask(__name__)

# Konfigurasi folder untuk menyimpan file yang diupload
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

reader = easyocr.Reader(['id'])

@app.route('/ocr', methods=['POST'])
def ocr():
    if 'file' not in request.files:
        return jsonify({
             'message': 'No file part in the request',
             'error': True,
             'OCR_result': []
             }), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({
             'message': 'No file selected for uploading',
             'error': True,
             'OCR_result': []
             }), 400
    if file:
        # Membaca file foto
        filename = secure_filename(file.filename)
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_path)

        # Inisialisasi OCR
        text = reader.readtext(file_path, paragraph=False, width_ths=5.0)

        # Menyimpan hasil OCR
        hasil = ''
        for match in text:
                hasil = hasil + match[1] + '\n'
        print(hasil)

        # Memanggil fungsi ekstraksi
        harga = re_harga(hasil)
        tanggal = re_tanggal(hasil)
        deskripsi = re_deskripsi(hasil)
        
        # Mengirim hasil ekstraksi
        return jsonify({
             'message': 'File successfully uploaded', 
             'error': False,
             'OCR_result': {
                  'harga' : harga,
                  'tanggal' : tanggal,
                  'deskripsi' : deskripsi
             }
            }), 200

def re_harga(text):
      # Pola Regex
      pattern_harga =  r'[TtJFf][Oo0][TtJFf][Aa4][LlI1].+'
      pattern_digit = r'\d[^a-zA-z]+\d[oO0][oO0]'

      # Mencari Text dengan pola
      result = ''
      word_harga = re.findall(pattern_harga, text)
      for match in word_harga:
          result += match + '\n'

      digit_harga = re.findall(pattern_digit, result)

      # Penyesuaian format
      if len(digit_harga) > 0:
        result = digit_harga[0]
        result = (result.replace(',', ''))
        result = (result.replace('.', ''))
        result = (result.replace(' ', ''))
        result = (result.replace('O', '0'))
        result = (result.replace('o', '0'))
        result = int(result)
        return result
      else: 
        result = 0
        return result

def re_tanggal(text):
      # Pola Regex
      pattern_tanggal = r'[0-3]\d.?[.,-]\d\d.?[.,-]\d\d'

      # Mencari Text dengan pola
      result = ''
      word_tanggal = re.findall(pattern_tanggal, text)

      # Penyesuaian format
      if len(word_tanggal) > 0:
        result = word_tanggal[0]
        result = result.replace(".", "-")
        result = result.replace(",", "-")
        result = result.replace(" ", "")

        a = result[:6]
        b = result[6:]
        result = a + '20' + b
        return result
      else:
        result = "-"
        return result
      
def re_deskripsi(text):
      # Pola Regex
      pattern_tanggal = r'[0-3]\d.?[.,]\d\d.?[.,]\d\d.+'
      pattern_total =  r'[TtJFf][Oo0][TtJFf][Aa4][LlI1].+'
      pattern_indomaret = r'[Iil1][Nn][Dd][Oo0][Mm][Aa4][Rr][Ee3][TtJ]'
      result = ''

      # Mencoba mendapatkan nama toko dan daftar belanja
      try :
          # Mencari nama toko
          try :
            word_indomaret = re.findall(pattern_indomaret, text)
            result = word_indomaret[0]

            # Penyesuaian hasil nama toko
            result = result.replace("i", "I")
            result = result.replace("l", "I")
            result = result.replace("1", "I")
            result = result.replace("n", "N")
            result = result.replace("d", "D")
            result = result.replace("0", "O")
            result = result.replace("o", "O")
            result = result.replace("m", "M")
            result = result.replace("4", "A")
            result = result.replace("a", "A")
            result = result.replace("r", "R")
            result = result.replace("e", "E")
            result = result.replace("3", "E")
            result = result.replace("t", "T")
            result = result.replace("J", "T")
          finally :
            # Mencari baris tanggal hingga baris total
            baris_tanggal = re.search(pattern_tanggal, text, re.MULTILINE)
            baris_total = re.search(pattern_total, text, re.MULTILINE)

            # Mendapatkan teks setelah baris tanggal hingga baris total
            if baris_tanggal and baris_total:
                start_tanggal = baris_tanggal.end()
                end_total = baris_total.end()
                teks_setelah_tanggal_sampai_total = text[start_tanggal:end_total]
                result += teks_setelah_tanggal_sampai_total
            else:
                teks_setelah_tanggal_sampai_total = "-"
                result += teks_setelah_tanggal_sampai_total
            
            return result
      except :
          result = "-"
          return result

if __name__ == '__main__':
    app.run(debug=True)
