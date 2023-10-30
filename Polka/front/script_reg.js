const form = document.getElementById('form');

function getFormValue(event)
{
   event.preventDefault();

   const name = form.querySelector('[name="InputUserName"]'),
         email = form.querySelector('[name="InputEmail1"]'),
         password = form.querySelector('[name="InputPassword1"]');
   const data =
   {
         name: name.value,
         email: email.value,
         password: password.value
   };
   let json = JSON.stringify(data);

   console.log(json);
}
  form.addEventListener('submit', getFormValue);
