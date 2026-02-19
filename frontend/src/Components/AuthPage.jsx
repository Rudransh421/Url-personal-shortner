import React, { useState } from "react";
import Registration from "./Registration";
import Login from "./Login";

function AuthPage() {
  const [showLogin, setShowLogin] = useState(false);

  return (
    <>
      {showLogin ? (
        <Login onSwitchToRegister={() => setShowLogin(false)} />
      ) : (
        <Registration onSwitchToLogin={() => setShowLogin(true)} />
      )}
    </>
  );
}

export default AuthPage;
