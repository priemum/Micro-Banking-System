from flask import Flask, escape, request,render_template

app = Flask(__name__)

@app.route('/')
def hello():
    return render_template('SavingsAccount.html')

@app.route('/about')
def about():
    return "<h1>About Page</h1>"

@app.route('/createuser')
def createuser():
    return render_template('createuser.html')

@app.route('/Fixedaccount')
def fixedaccount():
    return render_template('FixedAccount.html')

@app.route('/Savingsaccount')
def savingsaccount():
    return render_template('SavingsAccount.html')

@app.route('/stats')
def stats():
    return render_template('stats.html')

@app.route('/contact')
def contact():
    return render_template('Contact.html')
    
if __name__=='__main__':
    app.run(debug=True)