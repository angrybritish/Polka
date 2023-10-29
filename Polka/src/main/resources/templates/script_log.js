const form = document.getElementById('form2');

function getFormValue(event)
{
   event.preventDefault();

   const name = form.querySelector('[name="InputUserName"]'), //получаем поле name
         password = form.querySelector('[name="InputPassword1"]');

   const data =
   {
         name: name.value,
         password: password.value
   };
   let json = JSON.stringify(data);

   console.log(json);
}
 form.addEventListener('submit', getFormValue);
